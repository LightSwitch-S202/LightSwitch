package com.lightswitch.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.lightswitch.domain.dto.FlagResponse;
import com.lightswitch.domain.dto.SseResponse;
import com.lightswitch.domain.dto.SseType;

public class Flags {

	private static final Map<String, Flag> flags = new HashMap<>();

	private Flags() {
	}

	public static void addAllFlags(List<FlagResponse> data) {
		flags.clear();

		for (FlagResponse flagResponse : data) {
			flags.put(flagResponse.getTitle(), flagResponse.toFlag());
		}
	}

	public static Optional<Flag> getFlag(String flagKey) {
		return Optional.ofNullable(flags.get(flagKey));
	}

	/**
	 * add & update
	 */
	public static void addFlag(Flag flag) {
		flags.put(flag.getTitle(), flag);
	}

	public static void deleteFlag(String title) {
		flags.remove(title);
	}

	public static void clear() {
		flags.clear();
	}

	public static void event(SseResponse sseResponse) {
		SseType type = sseResponse.getType();
		FlagResponse data = sseResponse.getData();
		if (type.equals(SseType.CREATE)) {
			addFlag(data.toFlag());
		} else if (type.equals(SseType.UPDATE)) {
			addFlag(data.toFlag());
		} else if (type.equals(SseType.DELETE)) {
			deleteFlag(data.getTitle());
		}
	}
}
