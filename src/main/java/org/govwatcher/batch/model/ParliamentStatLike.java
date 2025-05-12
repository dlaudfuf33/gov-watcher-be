package org.govwatcher.batch.model;

public interface ParliamentStatLike {
    Long getTotalBills();
    Long getPassedBills();
    Long getTotalComments();
}