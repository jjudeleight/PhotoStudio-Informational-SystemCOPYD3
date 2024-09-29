package com.example.practica.DAO;

//Класс, который описывает имя, используемое для идентификации пользователя или пациента в системе.
public class FullName {
    private String fullName;
    private String firstName;
    private String lastName;

    public FullName(String fullName){
        this.fullName = fullName;
        splitFullName();
    }
    public FullName(String firstName, String lastName){
        this.fullName = firstName + " " + lastName;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    //Разделение полного имени на Имя и Фамилию
    private void splitFullName() {
        String[] names = fullName.split(" ");
        if (names.length >= 2) {
            firstName = names[0];
            lastName = names[1];
        } else {
            firstName = lastName = null; // Устанавливаем в null, чтобы избежать неинициализированных значений
        }
    }


    //Проверка состоит ли полное имя из двух слов.
    public boolean isFullNameCorrect(){
        if(fullName.contains(" "))
            return true;
        else
            return false;
    }

    public String getFullName() {
        return fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
