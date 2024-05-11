package com.lightswitch.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class PropertyTest {

	@Mock
	private Property property;

	private String propertyKey;
	private String propertyValue;

	@BeforeEach
	void createProperty(){
		propertyKey = "이름";
		propertyValue = "이승";
		property = new Property(propertyKey, propertyValue);
	}

	@Test
	void getProperty() {
		assertNotNull(property);
		assertThat(property.getProperty()).isEqualTo(propertyKey);
	}

	@Test
	void getData() {
		assertNotNull(property);
		assertThat(property.getData()).isEqualTo(propertyValue);
	}
}