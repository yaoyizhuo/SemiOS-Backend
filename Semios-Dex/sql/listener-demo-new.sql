--dex 分支的UniswapV2Factory
INSERT INTO `subscribe` (`contract_address`,`topics`,`from_block`,`receive_address`,`filter_id`,`create_time`,`trade_type`,`is_del`,`status`,`order_init`)
VALUES ('0x401f40dc102a4f7cf9419ac884ed8e00bcd4ca98','0x7fbabee1ee0da0be8f120336e3c57ba3f192db4e2d35a5bb92a4ab7ef8bfe59f','0x75F970','http://172.31.31.55:9283/transaction/call',NULL,'2022-04-22 00:00:00','D4APairCreated',0,0,220);

--dex分支的UniswapV2Router
INSERT INTO `subscribe` (`contract_address`,`topics`,`from_block`,`receive_address`,`filter_id`,`create_time`,`trade_type`,`is_del`,`status`,`order_init`)
VALUES ('0x5622Fb509e29A804b45CCeb05a6D735407350526','0xad5c4648','0x75F970',NULL,NULL,'2022-04-22 00:00:00','wethAddress',0,0,220);



INSERT INTO `subscribe` (`contract_address`,`topics`,`from_block`,`receive_address`,`filter_id`,`create_time`,`trade_type`,`is_del`,`status`,`order_init`)
VALUES ('0x5622Fb509e29A804b45CCeb05a6D735407350526','0xe7f26cdf45159dd0c7ff634efbd5459ca530ae48e45ea47d006bb6ab8b31dc35','0x75F970','http://172.31.31.55:9283/transaction/call',NULL,'2022-04-22 00:00:00','D4AMint',0,0,220);

INSERT INTO `subscribe` (`contract_address`,`topics`,`from_block`,`receive_address`,`filter_id`,`create_time`,`trade_type`,`is_del`,`status`,`order_init`)
VALUES ('0x5622Fb509e29A804b45CCeb05a6D735407350526','0x0b0b2391a60d56c106458a58b1bc30475f99318d4b04b91cec7e2eba733680af','0x75F970','http://172.31.31.55:9283/transaction/call',NULL,'2022-04-22 00:00:00','D4ASync',0,0,220);

INSERT INTO `subscribe` (`contract_address`,`topics`,`from_block`,`receive_address`,`filter_id`,`create_time`,`trade_type`,`is_del`,`status`,`order_init`)
VALUES ('0x5622Fb509e29A804b45CCeb05a6D735407350526','0x01ecf300005a210787cd5e5ca994087be9362c74c5ffef6c83475ec4fdebd9d2','0x75F970','http://172.31.31.55:9283/transaction/call',NULL,'2022-04-22 00:00:00','D4ABurn',0,0,220);

INSERT INTO `subscribe` (`contract_address`,`topics`,`from_block`,`receive_address`,`filter_id`,`create_time`,`trade_type`,`is_del`,`status`,`order_init`)
VALUES ('0x5622Fb509e29A804b45CCeb05a6D735407350526','0x28a3a211217061c19ad79ece1779dee92ed50532c4d35e5b2501c81d2010abfd','0x75F970','http://172.31.31.55:9283/transaction/call',NULL,'2022-04-22 00:00:00','D4ASwap',0,0,220);

