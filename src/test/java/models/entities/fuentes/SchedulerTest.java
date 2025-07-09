package models.entities.fuentes;

import models.services.Scheduler;

public class SchedulerTest {
    public static void main(String[] args) throws Exception {
        Scheduler scheduler = new Scheduler(false);
        scheduler.iniciarScheduler();}}
