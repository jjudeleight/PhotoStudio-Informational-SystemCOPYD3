package com.example.practica.DTO;

import java.sql.ResultSet;
import java.sql.SQLException;

// Описывает лекарства из базы данных, которые могут выдать пациенту.
public class Photo {
    private static int medicament_Id;
    private String nameStudiaColumn;
    private String OpisanieStudiiColumn;
    private String FormaFotossesiiColumn;
    private String countryColumn;
    private String callChasovColumn;
    private String Coli4estvoFotoColumn;

    // Конструктор с параметрами




    public Photo(int medicament_id, String nameStudiaColumn, String OpisanieStudiiColumn, String FormaFotossesiiColumn, String countryColumn, String CallChasovColumn, String Coli4estvoFotoColumn) {
        this.medicament_Id = medicament_id;
        this.nameStudiaColumn = nameStudiaColumn;
        this.OpisanieStudiiColumn = OpisanieStudiiColumn;
        this.FormaFotossesiiColumn = FormaFotossesiiColumn;
        this.countryColumn = countryColumn;
        this.callChasovColumn = CallChasovColumn;
        this.Coli4estvoFotoColumn = Coli4estvoFotoColumn ;
    }

    // Геттеры
    public static int getMedicament_Id() { return medicament_Id;}
    public String getNameStudiaColumn() {return nameStudiaColumn;}
    public String getOpisanieStudiiColumn() {return OpisanieStudiiColumn;}
    public String getFormaFotossesiiColumn() {return FormaFotossesiiColumn;}
    public String getCountryColumn() {return countryColumn;}
    public String getCallChasovColumn() {return callChasovColumn;}
    public String getColi4estvoFotoColumn() {return Coli4estvoFotoColumn;}

    // Конструктор для создания объекта из ResultSet
    public Photo(ResultSet set) throws SQLException {
        medicament_Id = set.getInt("medicament_id");
        nameStudiaColumn = set.getString("nameStudiaColumn");
        OpisanieStudiiColumn = set.getString("OpisanieStudiiColumn");
        FormaFotossesiiColumn = set.getString("FormaFotossesiiColumn");
        countryColumn = set.getString("countryColumn");
        callChasovColumn = set.getString("CallChasovColumn");
        Coli4estvoFotoColumn = set.getString("Coli4estvoFotoColumn");
    }
}
