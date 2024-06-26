package com.lightswitch.domain;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

public class FlagTest {

	private List<Keyword> keywords;
	private List<Variation> variations;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void 키워드_없는_Flag는_Portion으로_반환() {
		Flag noKeywordsFlag = getNoKeywordsFlag();

		Boolean booleanValue = noKeywordsFlag.getValue(getNoPropertyUser());
		Boolean booleanValue2 = noKeywordsFlag.getValue(getPropertyUser());

		assertThat(booleanValue).isFalse();
		assertThat(booleanValue2).isFalse();
	}

	@Test
	void 키워드_Flag는_키워드_Value로_반환() {
		Flag keywordsFlag = getKeywordsFlag();

		Boolean booleanValue = keywordsFlag.getValue(getNoPropertyUser());
		Boolean booleanValue2 = keywordsFlag.getValue(getPropertyUser());
		Boolean booleanValue3 = keywordsFlag.getValue(getDifPropertyUser());

		assertThat(booleanValue).isFalse();
		assertThat(booleanValue2).isTrue();
		assertThat(booleanValue3).isFalse();
	}

	@Test
	void 비활성화_Flag는_기본_값으로_반환() {
		Flag noKeywordsFlag = getNoKeywordsFlag();
		noKeywordsFlag.switchFlag(false);

		Boolean booleanValue = noKeywordsFlag.getValue(getNoPropertyUser());
		Boolean booleanValue2 = noKeywordsFlag.getValue(getPropertyUser());

		assertThat(booleanValue).isEqualTo(Boolean.parseBoolean(noKeywordsFlag.getDefaultValue()));
		assertThat(booleanValue2).isEqualTo(Boolean.parseBoolean(noKeywordsFlag.getDefaultValue()));
	}

	@Test
	void 비활성화_키워드_Flag는_기본_값으로_반환() {
		Flag keywordsFlag = getKeywordsFlag();
		keywordsFlag.switchFlag(false);

		Boolean booleanValue = keywordsFlag.getValue(getNoPropertyUser());
		Boolean booleanValue2 = keywordsFlag.getValue(getPropertyUser());

		assertThat(booleanValue).isEqualTo(Boolean.parseBoolean(keywordsFlag.getDefaultValue()));
		assertThat(booleanValue2).isEqualTo(Boolean.parseBoolean(keywordsFlag.getDefaultValue()));
	}

	@Test
	void Flag는_Value_타입으로_반환() {
		Flag booleanFlag = getFlagByType(FlagType.BOOLEAN, "true");
		Flag numberFlag = getFlagByType(FlagType.INTEGER, "1");
		Flag stringFlag = getFlagByType(FlagType.STRING, "string");

		assertThatCode(() -> {
			Boolean booleanValue = booleanFlag.getValue(getNoPropertyUser());
			Integer numberValue = numberFlag.getValue(getNoPropertyUser());
			String stringValue = stringFlag.getValue(getNoPropertyUser());
		}).doesNotThrowAnyException();
	}

	@Test
	void Flag는_Value_타입으로_반환_에러() {
		Flag booleanFlag = getFlagByType(FlagType.BOOLEAN, "true");
		Flag numberFlag = getFlagByType(FlagType.INTEGER, "1");
		Flag stringFlag = getFlagByType(FlagType.STRING, "string");

		assertThatThrownBy(() -> {
			Integer value = booleanFlag.getValue(getNoPropertyUser());
		}).isInstanceOf(ClassCastException.class);
		assertThatThrownBy(() -> {
			String value2 = booleanFlag.getValue(getNoPropertyUser());
		}).isInstanceOf(ClassCastException.class);
		assertThatThrownBy(() -> {
			Boolean value = numberFlag.getValue(getNoPropertyUser());
		}).isInstanceOf(ClassCastException.class);
		assertThatThrownBy(() -> {
			String value2 = numberFlag.getValue(getNoPropertyUser());

		}).isInstanceOf(ClassCastException.class);
		assertThatThrownBy(() -> {
			Boolean value = stringFlag.getValue(getNoPropertyUser());

		}).isInstanceOf(ClassCastException.class);
		assertThatThrownBy(() -> {
			Integer value2 = stringFlag.getValue(getNoPropertyUser());
		}).isInstanceOf(ClassCastException.class);
	}

	@Test
	void calValue_키워드에_따른_값_테스트() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		//given
		Flag keywordsFlag = getKeywordsFlag();
		LSUser propertyUser = getPropertyUser();
		LSUser noPropertyUser = getNoPropertyUser();

		//when
		Method calValue = Flag.class.getDeclaredMethod("calValue", LSUser.class);
		calValue.setAccessible(true);
		String propertyResult = (String)calValue.invoke(keywordsFlag, propertyUser);
		String noPropertyResult = (String)calValue.invoke(keywordsFlag, noPropertyUser);

		//then
		assertThat(propertyResult).isEqualTo("true");
		assertThat(noPropertyResult).isNotEqualTo("true");
	}

	@Test
	void getValueWithType_테스트() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		//given
		Flag booleanFlag = getFlagByType(FlagType.BOOLEAN, "true");
		Flag stringFlag = getFlagByType(FlagType.STRING, "string");
		Flag integerFlag = getFlagByType(FlagType.INTEGER, "1");

		//when
		Method calValue = Flag.class.getDeclaredMethod("getValueWithType", String.class);
		calValue.setAccessible(true);
		Boolean booleanResult = (Boolean)calValue.invoke(booleanFlag, "true");
		String stringResult = (String)calValue.invoke(stringFlag, "string");
		Integer integerResult = (Integer)calValue.invoke(integerFlag, "1");

		//then
		assertThat(booleanResult).isNotNull();
		assertThat(stringResult).isNotNull();
		assertThat(integerResult).isNotNull();
		assertThat(booleanResult).isEqualTo(true);
		assertThat(stringResult).isEqualTo("string");
		assertThat(integerResult).isEqualTo(1);
	}

	@Test
	void getValueWithType_예외_테스트() throws NoSuchMethodException {
		//given
		Flag booleanFlag = getFlagByType(FlagType.BOOLEAN, "true");
		Flag stringFlag = getFlagByType(FlagType.STRING, "string");
		Flag integerFlag = getFlagByType(FlagType.INTEGER, "1");

		//when
		Method getValueWithType = Flag.class.getDeclaredMethod("getValueWithType", String.class);
		getValueWithType.setAccessible(true);

		assertThatThrownBy(() -> {
			Integer value = (Integer)getValueWithType.invoke(booleanFlag, "true");
		}).isInstanceOf(ClassCastException.class);
		assertThatThrownBy(() -> {
			String value2 = (String)getValueWithType.invoke(booleanFlag, "true");
		}).isInstanceOf(ClassCastException.class);
		assertThatThrownBy(() -> {
			Boolean value = (Boolean)getValueWithType.invoke(stringFlag, "string");
		}).isInstanceOf(ClassCastException.class);
		assertThatThrownBy(() -> {
			Integer value2 = (Integer)getValueWithType.invoke(stringFlag, "string");
		}).isInstanceOf(ClassCastException.class);
		assertThatThrownBy(() -> {
			Boolean value = (Boolean)getValueWithType.invoke(integerFlag, "1");
		}).isInstanceOf(ClassCastException.class);
		assertThatThrownBy(() -> {
			String value2 = (String)getValueWithType.invoke(integerFlag, "1");
		}).isInstanceOf(ClassCastException.class);
	}

	private Flag getNoKeywordsFlag() {
		keywords = List.of();
		variations = List.of(new Variation(0, "false", 100, "des1"));

		return new Flag(1, "Boolean Flag", "", FlagType.BOOLEAN, keywords, "true", 0,
			"", variations, 1, "2020-01-01", "2021-01-01", null, true);
	}

	private Flag getKeywordsFlag() {
		keywords = List.of(new Keyword(List.of(new Property("이름", "이승"), new Property("직급", "팀원")), "true"));
		variations = List.of(new Variation(0, "false", 100, "des1"));

		return new Flag(1, "Boolean Flag", "", FlagType.BOOLEAN, keywords, "true", 0,
			"", variations, 1, "2020-01-01", "2021-01-01", null, true);
	}

	private Flag getFlagByType(FlagType flagType, String value) {
		variations = List.of(new Variation(0, "false", 0, "des1"));

		return new Flag(1, "Flag", "", flagType, keywords, value, 100,
			"", variations, 1, "2020-01-01", "2021-01-01", null, true);
	}

	private LSUser getNoPropertyUser() {
		return new LSUser.Builder("1")
			.build();
	}

	private LSUser getPropertyUser() {
		return new LSUser.Builder("1")
			.property("이름", "이승")
			.property("직급", "팀원")
			.build();
	}

	private LSUser getDifPropertyUser() {
		return new LSUser.Builder("1")
			.property("이름", "팀원")
			.property("직급", "이승")
			.build();
	}
}
