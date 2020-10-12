package com.example.stop_covid19.ModelHelper;

public class StatesModel {

    private String deaths, confirmed_cases, recovered, statename;

    public StatesModel() {
    }

    public StatesModel(String deaths, String confirmed_cases, String recovered, String statename) {
        this.deaths = deaths;
        this.confirmed_cases = confirmed_cases;
        this.recovered = recovered;
        this.statename = statename;
    }

    public String getDeaths() {
        return deaths;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    public String getConfirmed_cases() {
        return confirmed_cases;
    }

    public void setConfirmed_cases(String confirmed_cases) {
        this.confirmed_cases = confirmed_cases;
    }

    public String getRecovered() {
        return recovered;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }

    public String getStatename() {
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }
}
