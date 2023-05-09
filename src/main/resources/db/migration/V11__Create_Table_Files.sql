CREATE TABLE `files` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255),
    `content_type` VARCHAR(255),
    `size` BIGINT,
    `data` LONGBLOB,
    PRIMARY KEY (id)
);