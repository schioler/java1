package dk.schioler.economy.out.data;

import java.util.Date;
import java.util.List;

import dk.schioler.economy.model.Line;
import dk.schioler.economy.model.User;

public interface TimeLine {

   public abstract boolean addLine(Line line);

   public User getUser();

   public void setUser(User user);

   public abstract List<Period> getPeriods();

   public Date getStartDate();

   public Date getEndDate();

   public Date getActualMinDate();

   public Date getActualMaxDate();

   public void createTimelineForPeriod(Date startDate, Date endDate);

   public void clear();

}