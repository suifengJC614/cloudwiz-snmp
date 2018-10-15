
-- ------------------------------
-- Table channel_info for `设备品牌表`
-- ------------------------------
CREATE TABLE IF NOT EXISTS snmp_device_brand(
  sdb_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  sdb_name VARCHAR(200) NOT NULL COMMENT '品牌名称',
  sdb_description TEXT DEFAULT NULL COMMENT '描述',
  sdb_logo VARCHAR(100) NOT NULL COMMENT 'LOGO',
  sdb_classify INT UNSIGNED DEFAULT NULL COMMENT '分类',
  sdb_disable TINYINT DEFAULT 0 COMMENT '是否禁用',
  sdb_create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  sdb_update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (sdb_id),
  INDEX (sdb_classify)
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT '设备品牌表';

-- ------------------------------
-- Table channel_info for `SNMP_EXPORTER实例表`
-- ------------------------------
CREATE TABLE IF NOT EXISTS snmp_exporter_info(
  sei_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  sei_name VARCHAR(200) DEFAULT NULL COMMENT '实例名称',
  sei_local_address VARCHAR(50) NOT NULL COMMENT '安装机器IP',
  sei_client_address VARCHAR(50) NOT NULL COMMENT '客户端IP',
  sei_status TINYINT NOT NULL COMMENT '状态',
  sei_pause TINYINT DEFAULT 0 COMMENT '暂停监控',
  sei_last_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后访问时间',
  sei_create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  sei_update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (sei_id),
  INDEX (sei_client_address),
  INDEX (sei_name),
  INDEX (sei_status)
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT 'SNMP探针实例表';

-- ------------------------------
-- Table channel_info for `监控设备表`
-- ------------------------------
CREATE TABLE IF NOT EXISTS snmp_monitor_device(
  smd_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  smd_sdb_id INT UNSIGNED NOT NULL COMMENT '设备品牌',
  smd_sei_name VARCHAR(200) DEFAULT NULL COMMENT 'SNMP_EXPORTER实例名称',
  smd_name VARCHAR(200) DEFAULT NULL COMMENT '设备名称',
  smd_model_number VARCHAR(200) DEFAULT NULL COMMENT '设备型号',
  smd_type TINYINT DEFAULT NULL COMMENT '设备类型',
  smd_address VARCHAR(50) NOT NULL COMMENT '设备地址',
  smd_port INT DEFAULT 161 COMMENT '监控端口',
  smd_version TINYINT DEFAULT 2 COMMENT 'SNMP版本',
  smd_properties JSON DEFAULT NULL COMMENT 'SNMP配置属性',
  smd_create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  smd_update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (smd_id),
  INDEX (smd_sdb_id),
  INDEX (smd_sei_name)
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT '监控设备表';

-- ------------------------------
-- Table channel_info for `监控项表`
-- ------------------------------
CREATE TABLE IF NOT EXISTS snmp_monitor_items(
  smi_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  smi_sdb_id INT UNSIGNED NOT NULL COMMENT '设备品牌',
  smd_sei_name VARCHAR(200) DEFAULT NULL COMMENT 'SNMP_EXPORTER实例名称',
  smi_oid VARCHAR(200) NOT NULL COMMENT '监控项OID',
  smi_key VARCHAR(200) NOT NULL COMMENT '监控项键名',
  smi_value_unit VARCHAR(100) DEFAULT NULL COMMENT '值单位',
  smi_value_type TINYINT DEFAULT NULL COMMENT '值类型',
  smi_create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  smi_update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (smi_id),
  INDEX (smi_sdb_id),
  INDEX (smd_sei_name)
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT '监控项表';

-- ------------------------------
-- Table channel_info for `监控订阅表`
-- ------------------------------
CREATE TABLE IF NOT EXISTS snmp_monitor_subscribe(
  sms_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  sms_sei_id INT UNSIGNED NOT NULL COMMENT 'SNMP_EXPORTER实例',
  sms_subscribe VARCHAR(200) DEFAULT NULL COMMENT '订阅名称',
  sms_api_path VARCHAR(500) NOT NULL COMMENT '订阅HTTP接口路径',
  sms_disable TINYINT DEFAULT 0 COMMENT '是否禁用',
  sms_create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  sms_update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (sms_id),
  INDEX (sms_sei_id)
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT 'SNMP探针实例表';


-- ------------------------------
-- Table channel_info for `监控历史记录表`
-- ------------------------------
CREATE TABLE IF NOT EXISTS snmp_monitor_history(
  smh_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  smh_sei_id INT UNSIGNED NOT NULL COMMENT 'SNMP_EXPORTER实例',
  smh_target VARCHAR(500) NOT NULL COMMENT '监控数据发送目标',
  smh_device_count INT NOT NULL COMMENT '监控设备个数',
  smh_item_count INT NOT NULL COMMENT '监控项个数',
  smh_total_time BIGINT UNSIGNED NOT NULL COMMENT '监控总耗时(ms)',
  smh_create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (smh_id),
  INDEX (smh_sei_id)
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT '监控历史记录表';
