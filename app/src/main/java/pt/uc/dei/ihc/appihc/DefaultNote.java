package pt.uc.dei.ihc.appihc;

import java.io.Serializable;
import java.util.ArrayList;

public class DefaultNote implements Serializable {

   private String Author;
   private String City;
   private String Country;
   private String Title;
   private String Description;
   private String Address;
   private String ImageUrl;
   private Double Latitude;
   private Double Longitude;
   private boolean Privacy;
   private ArrayList<String> Emails;

   public DefaultNote(String author, String city, String country, String title, String description, String address, String imageUrl, Double latitude, Double longitude, boolean privacy, ArrayList<String> emails) {
      Author = author;
      City = city;
      Country = country;
      Title = title;
      Description = description;
      Address = address;
      ImageUrl = imageUrl;
      Latitude = latitude;
      Longitude = longitude;
      Privacy = privacy;
      Emails = emails;
   }

    public DefaultNote() {

    }

   public String getAuthor() {
      return Author;
   }

   public void setAuthor(String author) {
      Author = author;
   }

   public String getCity() {
      return City;
   }

   public void setCity(String city) {
      City = city;
   }

   public String getCountry() {
      return Country;
   }

   public void setCountry(String country) {
      Country = country;
   }

   public String getTitle() {
      return Title;
   }

   public void setTitle(String title) {
      Title = title;
   }

   public String getDescription() {
      return Description;
   }

   public void setDescription(String description) {
      Description = description;
   }

   public String getAddress() {
      return Address;
   }

   public void setAddress(String address) {
      Address = address;
   }

   public String getImageUrl() {
      return ImageUrl;
   }

   public void setImageUrl(String imageUrl) {
      ImageUrl = imageUrl;
   }

   public Double getLatitude() {
      return Latitude;
   }

   public void setLatitude(Double latitude) {
      Latitude = latitude;
   }

   public Double getLongitude() {
      return Longitude;
   }

   public void setLongitude(Double longitude) {
      Longitude = longitude;
   }

   public boolean isPrivacy() {
      return Privacy;
   }

   public void setPrivacy(boolean privacy) {
      Privacy = privacy;
   }

   public ArrayList<String> getEmails() {
      return Emails;
   }

   public void setEmails(ArrayList<String> emails) {
      Emails = emails;
   }

   @Override
   public String toString() {
      return "DefaultNote{" +
              "Author='" + Author + '\'' +
              ", City='" + City + '\'' +
              ", Country='" + Country + '\'' +
              ", Title='" + Title + '\'' +
              ", Description='" + Description + '\'' +
              ", Address='" + Address + '\'' +
              ", ImageUrl='" + ImageUrl + '\'' +
              ", Latitude=" + Latitude +
              ", Longitude=" + Longitude +
              ", Privacy=" + Privacy +
              ", Emails=" + Emails +
              '}';
   }
}





/*
1{
1Modelar a Classe (Done)
Implementar o Save
Conseguir Ler o Save
Retirar os dados para o save
};

2{
Sistema de Loading;
};

3{
Sistema de Lista;
Guardar um ponto inicial -> o utilizador afasta-se -> vai buscar mais dados | Por rua
}




 */
