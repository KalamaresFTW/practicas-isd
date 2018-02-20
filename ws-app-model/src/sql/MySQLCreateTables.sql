-- MiniUber Model --

DROP TABLE Trip;
DROP TABLE Driver;

-- Driver -- 

CREATE TABLE Driver (
	driverId BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(255) COLLATE latin1_bin NOT NULL, 
	city VARCHAR(255) COLLATE latin1_bin NOT NULL,
	vehicleModel VARCHAR(255) COLLATE latin1_bin NOT NULL,
	startHour SMALLINT NOT NULL,
	endHour SMALLINT NOT NULL,
	scoreCount INT NOT NULL DEFAULT 0,
	totalScore INT NOT NULL DEFAULT 0,
	registrationDate DATETIME NOT NULL,
	CONSTRAINT DriverPK PRIMARY KEY(driverId),
	CONSTRAINT validStartHour CHECK ( startHour >= 0 AND startHour <= 23 ),
	CONSTRAINT validEndHour CHECK ( endHour>= 0 AND endHour <= 23 )
) ENGINE = InnoDB;

-- Trip --

CREATE TABLE Trip (
	tripId BIGINT NOT NULL AUTO_INCREMENT,
	driverId BIGINT NOT NULL,
	userLogin VARCHAR(255) COLLATE latin1_bin NOT NULL,
	originLocation VARCHAR(255) COLLATE latin1_bin NOT NULL,
	destinationLocation VARCHAR(255) COLLATE latin1_bin NOT NULL,
	score SMALLINT NULL DEFAULT NULL,
	cardNumber CHAR(16) NOT NULL,
	bookingDate DATETIME NOT NULL,
	CONSTRAINT TripPK PRIMARY KEY(tripId),
	CONSTRAINT TripDriverIdFK FOREIGN KEY(driverId)
		REFERENCES Driver(driverId) ON DELETE CASCADE
) ENGINE = InnoDB;