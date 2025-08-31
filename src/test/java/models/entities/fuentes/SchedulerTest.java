package models.entities.fuentes;

import models.agregador.SchedulerAgregador;

public class SchedulerTest {
    public static void main(String[] args) throws Exception {
        SchedulerAgregador scheduler = new SchedulerAgregador(false);
        scheduler.iniciarScheduler();}}
