package io.github.eunhyun.eunhyunbot.api.repository;

import io.github.eunhyun.eunhyunbot.api.object.Warn;

public interface WarnRepository {

    Warn get(String id);
}