package cn.cloudwiz.dalian.snmp.nms;

import cn.cloudwiz.dalian.snmp.api.nms.SnmpManager;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Snmp4jManager implements SnmpManager {

    private static final String ADDRESS_TEMPLATE = "udp:%s/161";

    public void get() throws IOException {
        String ip = "192.168.0.101";
        int version = SnmpConstants.version2c;
        String community = "public";
        String[] oids = {
                "1.3.6.1.2.1.1.1.0",
                "1.3.6.1.2.1.1.2.0",
                "1.3.6.1.2.1.1.5.0"
        };

        String userName = "";


        TransportMapping transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);
        if (version == SnmpConstants.version3) {
            USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
            SecurityModels.getInstance().addSecurityModel(usm);
        }
        snmp.listen();

        Address address = GenericAddress.parse(String.format(ADDRESS_TEMPLATE, ip));

        Target target = null;
        if (version == SnmpConstants.version3) {
            OctetString securityName = new OctetString(userName);
            OctetString pwd = new OctetString("MD5DESUserAuthPassword");
            OctetString privpwd = new OctetString("MD5DESUserPrivPassword");
            UsmUser usmUser = new UsmUser(securityName, AuthMD5.ID, pwd, PrivDES.ID, privpwd);
            snmp.getUSM().addUser(securityName, usmUser);
            UserTarget userTarget = new UserTarget();
            userTarget.setSecurityLevel(SecurityLevel.AUTH_PRIV);
            userTarget.setSecurityName(usmUser.getSecurityName());
            target = userTarget;
        } else {
            CommunityTarget communityTarget = new CommunityTarget();
            communityTarget.setCommunity(new OctetString(community));
            target = communityTarget;
        }

        target.setVersion(version);
        target.setAddress(address);
        target.setRetries(5);
        target.setTimeout(1000);


        PDU pdu = new PDU();
        for (String oid : oids) {
            pdu.add(new VariableBinding(new OID(oid)));
        }
        pdu.setType(PDU.GET);

        ResponseEvent responseEvent = snmp.send(pdu, target);
        Address peerAddress = responseEvent.getPeerAddress();
        PDU response = responseEvent.getResponse();
        System.out.println(peerAddress);
        response.getVariableBindings().stream().forEach(item -> {
            System.out.println(String.format("%s = %s", ((VariableBinding) item).getOid(), ((VariableBinding) item).getVariable()));
        });

    }

    public void walk() throws IOException {
        String ip = "192.168.0.101";
        int version = SnmpConstants.version2c;
        String community = "public";
        String[] oids = {
                "1.3.6.1.2.1.1",
                "1.3.6.1.2.1.2"
        };

        TransportMapping transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);
        if (version == SnmpConstants.version3) {
            USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
            SecurityModels.getInstance().addSecurityModel(usm);
        }
        transport.listen();

        Address address = GenericAddress.parse(String.format(ADDRESS_TEMPLATE, ip));

        Target target = null;
        if (version == SnmpConstants.version3) {
            OctetString securityName = new OctetString("MD5DES");
            OctetString pwd = new OctetString("MD5DESUserAuthPassword");
            OctetString privpwd = new OctetString("MD5DESUserPrivPassword");
            snmp.getUSM().addUser(securityName, new UsmUser(securityName, AuthMD5.ID, pwd, PrivDES.ID, privpwd));
            UserTarget userTarget = new UserTarget();
            userTarget.setSecurityLevel(SecurityLevel.AUTH_PRIV);
            userTarget.setSecurityName(securityName);
            target = userTarget;
        } else {
            CommunityTarget communityTarget = new CommunityTarget();
            communityTarget.setCommunity(new OctetString(community));
            target = communityTarget;
        }

        target.setVersion(version);
        target.setAddress(address);
        target.setRetries(5);
        target.setTimeout(1000);


        for (String oid : oids) {
            PDU pdu = new PDU();
            OID targetOID = new OID(oid);
            pdu.add(new VariableBinding(targetOID));

            while (true) {
                ResponseEvent respEvent = snmp.getNext(pdu, target);
                PDU response = respEvent.getResponse();
                if (response == null) {
                    break;
                } else {
                    VariableBinding vb = response.get(0);
                    System.out.println(String.format("%s = %s", vb.getOid(), vb.getVariable()));
                    if (checkWalkFinished(targetOID, pdu, vb)) {
                        break;
                    }
                    pdu.setRequestID(new Integer32(0));
                    pdu.set(0, vb);
                }

            }
        }

        System.out.println("==============snmp end================");
        snmp.close();
    }

    private boolean checkWalkFinished(OID targetOID, PDU pdu, VariableBinding vb) {
        boolean finished = false;
        if (pdu.getErrorStatus() != 0) {
            System.out.println("[true] responsePDU.getErrorStatus() != 0 ");
            System.out.println(pdu.getErrorStatusText());
            finished = true;
        } else if (vb.getOid() == null) {
            System.out.println("[true] vb.getOid() == null");
            finished = true;
        } else if (vb.getOid().size() < targetOID.size()) {
            System.out.println("[true] vb.getOid().size() < targetOID.size()");
            finished = true;
        } else if (targetOID.leftMostCompare(targetOID.size(), vb.getOid()) != 0) {
            System.out.println("[true] targetOID.leftMostCompare() != 0");
            finished = true;
        } else if (Null.isExceptionSyntax(vb.getVariable().getSyntax())) {
            System.out.println("[true] Null.isExceptionSyntax(vb.getVariable().getSyntax())");
            finished = true;
        } else if (vb.getOid().compareTo(targetOID) <= 0) {
            System.out.println("[true] Variable received is not " + "lexicographic successor of requested " + "one:");
            System.out.println(vb.toString() + " <= " + targetOID);
            finished = true;
        }
        return finished;
    }


}
