package com.example.stop_covid19.ModelHelper;

import com.android.volley.toolbox.StringRequest;

public class CountryModel {

private String flag, country, cases, today_cases, deaths, today_deaths, recovered, active, population, continent;

    public CountryModel() {
    }

    public CountryModel(String flag, String country, String cases, String today_cases, String deaths, String today_deaths, String recovered, String active, String population, String continent) {
        this.flag = flag;
        this.country = country;
        this.cases = cases;
        this.today_cases = today_cases;
        this.deaths = deaths;
        this.today_deaths = today_deaths;
        this.recovered = recovered;
        this.active = active;
        this.population = population;
        this.continent = continent;
    }


    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }

    public String getToday_cases() {
        return today_cases;
    }

    public void setToday_cases(String today_cases) {
        this.today_cases = today_cases;
    }

    public String getDeaths() {
        return deaths;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    public String getToday_deaths() {
        return today_deaths;
    }

    public void setToday_deaths(String today_deaths) {
        this.today_deaths = today_deaths;
    }

    public String getRecovered() {
        return recovered;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }
}
