CREATE TABLE `oauth_access_token` (
`authentication_id` varchar(255) NOT NULL,
`token_id` varchar(255) NOT NULL,
`token` blob NOT NULL,
`user_name` varchar(255) NOT NULL,
`client_id` varchar(255) NOT NULL,
`authentication` blob NOT NULL,
`refresh_token` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `oauth_access_token`
ADD PRIMARY KEY (`authentication_id`);


CREATE TABLE `oauth_refresh_token` (
`token_id` varchar(255) NOT NULL,
`token` blob NOT NULL,
`authentication` blob NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;