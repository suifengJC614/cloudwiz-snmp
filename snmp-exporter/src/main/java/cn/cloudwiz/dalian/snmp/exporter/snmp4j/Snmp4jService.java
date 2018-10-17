package cn.cloudwiz.dalian.snmp.exporter.snmp4j;

import cn.cloudwiz.dalian.snmp.api.device.*;
import cn.cloudwiz.dalian.snmp.exporter.SnmpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class Snmp4jService implements SnmpService {

    private static final Logger LOGGER = LoggerFactory.getLogger(Snmp4jService.class);

    private static final String ADDRESS_TEMPLATE = "udp:%s/%s";

    @Override
    public Map<String, String> get(MonitorDevice device, List<String> oids) throws IOException {
        Snmp snmp = null;
        try {
            snmp = createSnmp(device);

            String address = String.format(ADDRESS_TEMPLATE, device.getAddress(), device.getPort());
            Target target = createTarget(device);
            target.setAddress(GenericAddress.parse(address));
            target.setRetries(5);
            target.setTimeout(1000);

            PDU pdu = createPDU(device.getVersion());
            oids.parallelStream().forEach(oid -> {
                pdu.add(new VariableBinding(new OID(oid)));
            });
            pdu.setType(PDU.GET);

            ResponseEvent responseEvent = snmp.send(pdu, target);
            PDU response = responseEvent.getResponse();
            if(response == null){
                return Collections.emptyMap();
            }
            Vector<? extends VariableBinding> bindings = response.getVariableBindings();
            if(bindings == null){
                return Collections.emptyMap();
            }
            return bindings.stream().collect(Collectors.toMap(
                    item -> item.getOid().toDottedString(),
                    item -> item.getVariable().toString()
            ));
        } finally {
            if (snmp != null) {
                snmp.close();
            }
        }
    }


    @Override
    public Map<String, String> walk(MonitorDevice device, List<String> oids) throws IOException {
        Snmp snmp = null;
        try {
            snmp = createSnmp(device);

            String address = String.format(ADDRESS_TEMPLATE, device.getAddress(), device.getPort());
            Target target = createTarget(device);
            target.setAddress(GenericAddress.parse(address));
            target.setRetries(5);
            target.setTimeout(1000);

            Map<String, String> result = new HashMap<>();
            for (String oid : oids) {
                PDU pdu = createPDU(device.getVersion());
                OID targetOID = new OID(oid);
                pdu.add(new VariableBinding(targetOID));

                ResponseEvent respEvent = snmp.getNext(pdu, target);
                PDU response = respEvent.getResponse();

                while ((response = snmp.getNext(pdu, target).getResponse()) != null) {
                    if (response.size() == 0) {
                        break;
                    }
                    VariableBinding vb = response.get(0);
                    result.put(vb.getOid().toString(), vb.getVariable().toString());
                    if (checkWalkFinished(targetOID, pdu, vb)) {
                        break;
                    }
                    pdu.clear();
                    pdu.add(vb);
                }
            }
            return result;
        } finally {
            if (snmp != null) {
                snmp.close();
            }
        }
    }

    private boolean checkWalkFinished(OID targetOID, PDU pdu, VariableBinding vb) {
        boolean finished = false;
        if (pdu.getErrorStatus() != 0) {
            LOGGER.warn(String.format("snmp walk pdu error: %s", pdu.getErrorStatusText()));
            return true;
        } else if (vb.getOid() == null) {
            LOGGER.debug("snmp walk finished. vb.getOid() == null");
            return true;
        } else if (vb.getOid().size() < targetOID.size()) {
            LOGGER.debug("snmp walk finished. vb.getOid().size() < targetOID.size()");
            return true;
        } else if (targetOID.leftMostCompare(targetOID.size(), vb.getOid()) != 0) {
            LOGGER.debug("snmp walk finished. targetOID.leftMostCompare() != 0");
            return true;
        } else if (Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
            LOGGER.debug("snmp walk finished. Null.isExceptionSyntax(vb.getVariable().getSyntax())");
            return true;
        } else if (vb.getOid().compareTo(targetOID) <= 0) {
            LOGGER.debug(String.format("snmp walk finished. Variable received is not lexicographic successor of requested one: %s <= %s",
                    vb, targetOID));
            return true;
        }
        return false;
    }

    protected Snmp createSnmp(MonitorDevice device) throws IOException {
        TransportMapping transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);
        if (Objects.equals(device.getVersion(), SnmpVersion.VERSION_3)) {
            Assert.isTrue(device instanceof SecurityDevice, "SNMPv3 device type must be SecurityDevice");
            SecurityDevice securityDevice = (SecurityDevice) device;

            OctetString securityName = new OctetString(securityDevice.getSecurityName());
            OID authType = parseAuthType(securityDevice.getAuthType());
            OctetString authpwd = new OctetString(securityDevice.getAuthPassphrase());
            OID privacyType = parsePrivacyType(securityDevice.getPrivacyType());
            OctetString privacypwd = new OctetString(securityDevice.getPrivacyPassphrase());
            UsmUser usmUser = new UsmUser(securityName, authType, authpwd, privacyType, privacypwd);
            USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
            usm.addUser(usmUser);
            SecurityModels.getInstance().addSecurityModel(usm);
        }
        snmp.listen();
        return snmp;
    }

    protected Target createTarget(MonitorDevice device) {
        if (Objects.equals(device.getVersion(), SnmpVersion.VERSION_3)) {
            Assert.isTrue(device instanceof SecurityDevice, "SNMPv3 device type must be SecurityDevice");
            SecurityDevice securityDevice = (SecurityDevice) device;
            UserTarget userTarget = new UserTarget();
            userTarget.setSecurityLevel(SecurityLevel.AUTH_PRIV);
            userTarget.setSecurityName(new OctetString(securityDevice.getSecurityName()));
            userTarget.setVersion(parseVersion(device.getVersion()));
            return userTarget;
        } else {
            CommunityTarget communityTarget = new CommunityTarget();
            if (device instanceof CommunityDevice) {
                String community = ((CommunityDevice) device).getCommunity();
                communityTarget.setCommunity(new OctetString(community));
            }
            return communityTarget;
        }
    }

    protected PDU createPDU(SnmpVersion version) {
        PDU result;
        if (Objects.equals(version, SnmpVersion.VERSION_3)) {
            result = new ScopedPDU();
        } else {
            result = new PDUv1();
        }
        return result;
    }

    private int parseVersion(SnmpVersion version) {
        switch (version) {
            case VERSION_1:
                return SnmpConstants.version1;
            case VERSION_2C:
                return SnmpConstants.version2c;
            case VERSION_3:
                return SnmpConstants.version3;
            default:
                return SnmpConstants.version2c;
        }
    }

    private OID parseAuthType(AuthType authType) {
        return AuthMD5.ID;
    }

    private OID parsePrivacyType(PrivacyType privacyType) {
        return PrivDES.ID;
    }

}
