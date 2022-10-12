DROP TABLE IF EXISTS `brand`;
DROP TABLE IF EXISTS `cart`;
DROP TABLE IF EXISTS `category`;
DROP TABLE IF EXISTS `coupon`;
DROP TABLE IF EXISTS `curation`;
DROP TABLE IF EXISTS `curationproduct`;
DROP TABLE IF EXISTS `member`;
DROP TABLE IF EXISTS `notice`;
DROP TABLE IF EXISTS `orderproduct`;
DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS `payment`;
DROP TABLE IF EXISTS `product`;
DROP TABLE IF EXISTS `productimage`;
DROP TABLE IF EXISTS `question`;
DROP TABLE IF EXISTS `questionanswer`;
DROP TABLE IF EXISTS `refreshtoken`;
DROP TABLE IF EXISTS `review`;
DROP TABLE IF EXISTS `reviewimage`;
DROP TABLE IF EXISTS `wishlist`;

CREATE TABLE `Brand` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `brandName` varchar(255) DEFAULT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Cart` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `createdDate` datetime DEFAULT NULL,
                        `updatedDate` datetime DEFAULT NULL,
                        `productCount` int DEFAULT NULL,
                        `member_id` bigint DEFAULT NULL,
                        `order_id` bigint DEFAULT NULL,
                        `product_id` bigint DEFAULT NULL,
                        PRIMARY KEY (`id`),
                        KEY `FK62b80wq5in3dmemy7ac86n6b1` (`member_id`),
                        KEY `FKqfbpndgl6gyqqolt2688ql1s9` (`order_id`),
                        KEY `FK1prjnseb2sioiqy35ijp0upfd` (`product_id`),
                        CONSTRAINT `FK1prjnseb2sioiqy35ijp0upfd` FOREIGN KEY (`product_id`) REFERENCES `Product` (`id`),
                        CONSTRAINT `FK62b80wq5in3dmemy7ac86n6b1` FOREIGN KEY (`member_id`) REFERENCES `Member` (`id`),
                        CONSTRAINT `FKqfbpndgl6gyqqolt2688ql1s9` FOREIGN KEY (`order_id`) REFERENCES `Orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Category` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `categoryName` varchar(30) NOT NULL,
                            `parent_id` bigint DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            KEY `FK5s5t2pfpxo0vnd1ihc43721ty` (`parent_id`),
                            CONSTRAINT `FK5s5t2pfpxo0vnd1ihc43721ty` FOREIGN KEY (`parent_id`) REFERENCES `Category` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Coupon` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `createdDate` datetime DEFAULT NULL,
                          `updatedDate` datetime DEFAULT NULL,
                          `couponStatus` varchar(255) DEFAULT NULL,
                          `couponTitle` varchar(255) DEFAULT NULL,
                          `discountType` varchar(255) DEFAULT NULL,
                          `discountValue` int DEFAULT NULL,
                          `expirationDate` datetime DEFAULT NULL,
                          `leastRequiredValue` int DEFAULT NULL,
                          `maxDiscountValue` int DEFAULT NULL,
                          `member_id` bigint DEFAULT NULL,
                          PRIMARY KEY (`id`),
                          KEY `FKkf5xfwcygq8v7uyx7jmhksmfm` (`member_id`),
                          CONSTRAINT `FKkf5xfwcygq8v7uyx7jmhksmfm` FOREIGN KEY (`member_id`) REFERENCES `Member` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Curation` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `createdDate` datetime DEFAULT NULL,
                            `updatedDate` datetime DEFAULT NULL,
                            `content` varchar(255) DEFAULT NULL,
                            `curationForm` varchar(255) DEFAULT NULL,
                            `curationImg` varchar(255) DEFAULT NULL,
                            `display` bit(1) DEFAULT NULL,
                            `title` varchar(255) DEFAULT NULL,
                            `member_id` bigint DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            KEY `FKig0js4m4h43blb6jc3i44bk0n` (`member_id`),
                            CONSTRAINT `FKig0js4m4h43blb6jc3i44bk0n` FOREIGN KEY (`member_id`) REFERENCES `Member` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `CurationProduct` (
                                   `id` bigint NOT NULL AUTO_INCREMENT,
                                   `createdDate` datetime DEFAULT NULL,
                                   `updatedDate` datetime DEFAULT NULL,
                                   `curation_id` bigint DEFAULT NULL,
                                   `product_id` bigint DEFAULT NULL,
                                   PRIMARY KEY (`id`),
                                   KEY `FKmtnpogrkcp7pkpinh4d6txsfj` (`curation_id`),
                                   KEY `FK13y17muh9o2cnh0n3kfk21kfi` (`product_id`),
                                   CONSTRAINT `FK13y17muh9o2cnh0n3kfk21kfi` FOREIGN KEY (`product_id`) REFERENCES `Product` (`id`),
                                   CONSTRAINT `FKmtnpogrkcp7pkpinh4d6txsfj` FOREIGN KEY (`curation_id`) REFERENCES `Curation` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Member` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `createdDate` datetime DEFAULT NULL,
                          `updatedDate` datetime DEFAULT NULL,
                          `authority` varchar(255) DEFAULT NULL,
                          `buildingName` varchar(255) DEFAULT NULL,
                          `detailAddress` varchar(255) DEFAULT NULL,
                          `email` varchar(255) DEFAULT NULL,
                          `firstKana` varchar(255) DEFAULT NULL,
                          `firstName` varchar(255) DEFAULT NULL,
                          `lastKana` varchar(255) DEFAULT NULL,
                          `lastName` varchar(255) DEFAULT NULL,
                          `password` varchar(255) DEFAULT NULL,
                          `phoneNum` varchar(255) DEFAULT NULL,
                          `point` int DEFAULT NULL,
                          `postalCode` varchar(255) DEFAULT NULL,
                          `provider` varchar(255) DEFAULT NULL,
                          `providerId` varchar(255) DEFAULT NULL,
                          `roadAddress` varchar(255) DEFAULT NULL,
                          `username` varchar(255) DEFAULT NULL,
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `UK_t0ltsb5tkx447nnr5ss6itepq` (`phoneNum`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Notice` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `createdDate` datetime DEFAULT NULL,
                          `updatedDate` datetime DEFAULT NULL,
                          `content` longtext,
                          `noticeImg` varchar(255) DEFAULT NULL,
                          `title` varchar(255) DEFAULT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `OrderProduct` (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `createdDate` datetime DEFAULT NULL,
                                `updatedDate` datetime DEFAULT NULL,
                                `count` int NOT NULL,
                                `orderPrice` int NOT NULL,
                                `order_id` bigint DEFAULT NULL,
                                `product_id` bigint DEFAULT NULL,
                                PRIMARY KEY (`id`),
                                KEY `FKnykdhqsceifdxa6g52uwfgsjq` (`order_id`),
                                KEY `FKlq4ktfv77v57pfvig8dxc9qo0` (`product_id`),
                                CONSTRAINT `FKlq4ktfv77v57pfvig8dxc9qo0` FOREIGN KEY (`product_id`) REFERENCES `Product` (`id`),
                                CONSTRAINT `FKnykdhqsceifdxa6g52uwfgsjq` FOREIGN KEY (`order_id`) REFERENCES `Orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=238 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Orders` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `createdDate` datetime DEFAULT NULL,
                          `updatedDate` datetime DEFAULT NULL,
                          `buildingName` varchar(255) DEFAULT NULL,
                          `deliveryMeasure` varchar(255) DEFAULT NULL,
                          `deliveryMessage` varchar(255) DEFAULT NULL,
                          `detailAddress` varchar(255) DEFAULT NULL,
                          `email` varchar(255) DEFAULT NULL,
                          `firstKana` varchar(255) DEFAULT NULL,
                          `firstName` varchar(255) DEFAULT NULL,
                          `lastKana` varchar(255) DEFAULT NULL,
                          `lastName` varchar(255) DEFAULT NULL,
                          `orderDate` datetime DEFAULT NULL,
                          `orderStatus` varchar(255) DEFAULT NULL,
                          `phoneNum` varchar(255) DEFAULT NULL,
                          `postalCode` varchar(255) DEFAULT NULL,
                          `roadAddress` varchar(255) DEFAULT NULL,
                          `member_id` bigint DEFAULT NULL,
                          PRIMARY KEY (`id`),
                          KEY `FKleg7wy1ic99waq6gm71sf8tdc` (`member_id`),
                          CONSTRAINT `FKleg7wy1ic99waq6gm71sf8tdc` FOREIGN KEY (`member_id`) REFERENCES `Member` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=125 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Payment` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `createdDate` datetime DEFAULT NULL,
                           `updatedDate` datetime DEFAULT NULL,
                           `acquirePoint` int DEFAULT NULL,
                           `deliveryPrice` int DEFAULT NULL,
                           `discount` int DEFAULT NULL,
                           `mileage` int DEFAULT NULL,
                           `paymentAmount` int DEFAULT NULL,
                           `paymentMeasure` varchar(255) DEFAULT NULL,
                           `productPrice` int DEFAULT NULL,
                           `coupon_id` bigint DEFAULT NULL,
                           `order_id` bigint DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           KEY `FK3oywfaw6i8soaolciokaivu1m` (`coupon_id`),
                           KEY `FK1d0iuw74434davw84m7hjuadq` (`order_id`),
                           CONSTRAINT `FK1d0iuw74434davw84m7hjuadq` FOREIGN KEY (`order_id`) REFERENCES `Orders` (`id`),
                           CONSTRAINT `FK3oywfaw6i8soaolciokaivu1m` FOREIGN KEY (`coupon_id`) REFERENCES `Coupon` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Product` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `createdDate` datetime DEFAULT NULL,
                           `updatedDate` datetime DEFAULT NULL,
                           `bookmark` bit(1) DEFAULT NULL,
                           `content` varchar(255) DEFAULT NULL,
                           `discountCheck` bit(1) DEFAULT NULL,
                           `discountEndDate` date DEFAULT NULL,
                           `discountStartDate` date DEFAULT NULL,
                           `discountValue` int DEFAULT NULL,
                           `originalPrice` int DEFAULT NULL,
                           `price` int DEFAULT NULL,
                           `productName` varchar(255) DEFAULT NULL,
                           `productStatus` varchar(255) DEFAULT NULL,
                           `purchaseCount` int DEFAULT NULL,
                           `quantity` int DEFAULT NULL,
                           `rating` float DEFAULT NULL,
                           `brand_id` bigint DEFAULT NULL,
                           `category_id` bigint DEFAULT NULL,
                           `discountStatus` varchar(255) DEFAULT NULL,
                           `discountType` varchar(255) DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           KEY `FKcbnyvs2x321b8yw2vi398b26h` (`brand_id`),
                           KEY `FKexqqeaksnmmku5py194ywp140` (`category_id`),
                           CONSTRAINT `FKcbnyvs2x321b8yw2vi398b26h` FOREIGN KEY (`brand_id`) REFERENCES `Brand` (`id`) ON DELETE CASCADE,
                           CONSTRAINT `FKexqqeaksnmmku5py194ywp140` FOREIGN KEY (`category_id`) REFERENCES `Category` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `ProductImage` (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `imageName` varchar(255) DEFAULT NULL,
                                `product_id` bigint DEFAULT NULL,
                                PRIMARY KEY (`id`),
                                KEY `FK6ch4gkmymtkgrhncc35bhyrmf` (`product_id`),
                                CONSTRAINT `FK6ch4gkmymtkgrhncc35bhyrmf` FOREIGN KEY (`product_id`) REFERENCES `Product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Question` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `createdDate` datetime DEFAULT NULL,
                            `updatedDate` datetime DEFAULT NULL,
                            `answerStatus` bit(1) DEFAULT NULL,
                            `content` varchar(255) DEFAULT NULL,
                            `secret` bit(1) DEFAULT NULL,
                            `title` varchar(255) DEFAULT NULL,
                            `member_id` bigint DEFAULT NULL,
                            `product_id` bigint DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            KEY `FKfxg3d3tt6ddyrlguyf0ubsqgb` (`member_id`),
                            KEY `FKb42wf8k36v6w73rtm6bqvdvbr` (`product_id`),
                            CONSTRAINT `FKb42wf8k36v6w73rtm6bqvdvbr` FOREIGN KEY (`product_id`) REFERENCES `Product` (`id`),
                            CONSTRAINT `FKfxg3d3tt6ddyrlguyf0ubsqgb` FOREIGN KEY (`member_id`) REFERENCES `Member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `QuestionAnswer` (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `createdDate` datetime DEFAULT NULL,
                                  `updatedDate` datetime DEFAULT NULL,
                                  `answer` varchar(255) DEFAULT NULL,
                                  `member_id` bigint DEFAULT NULL,
                                  `question_id` bigint DEFAULT NULL,
                                  PRIMARY KEY (`id`),
                                  KEY `FKd1m2j62n38rgpsns6mu6vvyd9` (`member_id`),
                                  KEY `FKc0uurbbwpjbvrhf8lkd603oy8` (`question_id`),
                                  CONSTRAINT `FKc0uurbbwpjbvrhf8lkd603oy8` FOREIGN KEY (`question_id`) REFERENCES `Question` (`id`),
                                  CONSTRAINT `FKd1m2j62n38rgpsns6mu6vvyd9` FOREIGN KEY (`member_id`) REFERENCES `Member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `RefreshToken` (
                                `refresh_key` varchar(255) NOT NULL,
                                `refresh_value` varchar(255) DEFAULT NULL,
                                PRIMARY KEY (`refresh_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Review` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `createdDate` datetime DEFAULT NULL,
                          `updatedDate` datetime DEFAULT NULL,
                          `content` varchar(255) DEFAULT NULL,
                          `rate` float DEFAULT NULL,
                          `title` varchar(255) DEFAULT NULL,
                          `member_id` bigint DEFAULT NULL,
                          `product_id` bigint DEFAULT NULL,
                          PRIMARY KEY (`id`),
                          KEY `FKdgh7i1rmiffemo1wxkea75tv3` (`member_id`),
                          KEY `FK4j47rrl7uw14px7jb4egtaoxq` (`product_id`),
                          CONSTRAINT `FK4j47rrl7uw14px7jb4egtaoxq` FOREIGN KEY (`product_id`) REFERENCES `Product` (`id`),
                          CONSTRAINT `FKdgh7i1rmiffemo1wxkea75tv3` FOREIGN KEY (`member_id`) REFERENCES `Member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `ReviewImage` (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `imageName` varchar(255) DEFAULT NULL,
                               `review_id` bigint DEFAULT NULL,
                               PRIMARY KEY (`id`),
                               KEY `FKpwxjgck7wivqqjgk4uxkscyy1` (`review_id`),
                               CONSTRAINT `FKpwxjgck7wivqqjgk4uxkscyy1` FOREIGN KEY (`review_id`) REFERENCES `Review` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Wishlist` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `createdDate` datetime DEFAULT NULL,
                            `updatedDate` datetime DEFAULT NULL,
                            `member_id` bigint DEFAULT NULL,
                            `product_id` bigint DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            KEY `FKsekxa7538iwq46w88r04dko2g` (`member_id`),
                            KEY `FKm4dcpslidmyxx6a4572hm1740` (`product_id`),
                            CONSTRAINT `FKm4dcpslidmyxx6a4572hm1740` FOREIGN KEY (`product_id`) REFERENCES `Product` (`id`),
                            CONSTRAINT `FKsekxa7538iwq46w88r04dko2g` FOREIGN KEY (`member_id`) REFERENCES `Member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
