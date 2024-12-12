ALTER TABLE plc_connection_hist DEFAULT CHARSET = UTF8;

ALTER TABLE plc_connection_hist CONVERT TO character SET UTF8;

# no error from springgoot without below commands after mariadb version upgrade
# show VARIABLES LIKE 'tx%';
# SET global tx_isolation = 'READ-COMMITTED';
# COMMIT;