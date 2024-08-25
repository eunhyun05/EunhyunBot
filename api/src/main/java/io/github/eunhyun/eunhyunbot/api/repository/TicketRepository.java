package io.github.eunhyun.eunhyunbot.api.repository;

public interface TicketRepository {

    long incrementTicketCount();

    long getTicketCount();
}