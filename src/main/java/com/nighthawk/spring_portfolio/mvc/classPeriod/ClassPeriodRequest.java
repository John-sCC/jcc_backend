package com.nighthawk.spring_portfolio.mvc.classPeriod;

public class ClassPeriodRequest {
    private String name;
    private long[] leaderIds;
    private long[] studentIds;

    public ClassPeriodRequest(String name, long[] leaderIds, long[] studentIds) {
        this.name = name;
        this.leaderIds = leaderIds;
        this.studentIds = studentIds;
    }

    public String getName()
    {
        return this.name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public long[] getLeaderIds()
    {
        return this.leaderIds;
    }
    public void setLeaderIds(long[] leaderIds)
    {
        this.leaderIds = leaderIds;
    }
    public long[] getStudentIds()
    {
        return this.studentIds;
    }
    public void setStudentIds(long[] studentIds)
    {
        this.studentIds = studentIds;
    }
}
