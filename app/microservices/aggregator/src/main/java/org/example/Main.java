package org.example;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.Time;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Timer t = new Timer();
        t.schedule(new Aggregate(), 0, 5000);
    }
}

class Student {
    public final int id;
    public final String name, surname;
    public Student(
            int id,
            String name,
            String surname
    ){
        this.id = id;
        this.name = name;
        this.surname = surname;
    }
}

class Grade{
    public final int id, value, studentId;
    public Grade(
            int id,
            int value,
            int studentId
    ){
        this.id = id;
        this.value = value;
        this.studentId = studentId;
    }
}

class StudentsResponse{
    public final Student[] result;
    public final boolean success;
    public StudentsResponse(
            Student[] result
    ){
        this.result = result;
        this.success = true;
    }
}

class GradesResponse{
    public final Grade[] result;
    public final boolean success;
    public GradesResponse(
            Grade[] result
    ){
        this.result = result;
        this.success = true;
    }
}

class ReportCard {
    private final String studentFullName;
    private final List<Grade> studentGrades;
    public ReportCard(String studentFullName, List<Grade> grades){
        this.studentFullName = studentFullName;
        this.studentGrades = grades;
    }

    @Override
    public String toString() {
        return "ReportCard{" +
                "studentFullName='" + studentFullName + '\'' +
                ", studentGrades=" + studentGrades.stream().map(g -> g.value).toList() +
                ", avg = " + studentGrades.stream().map(g -> (double) g.value / studentGrades.size()).reduce(0.0, Double::sum) +
                '}';
    }
}

class Aggregate extends TimerTask {

    private final OkHttpClient client = new OkHttpClient();

    private <C> C fetchData(String url, Class<C> classObj) throws MalformedURLException, IOException {
        Request req = new Request.Builder().url(url).build();
        ResponseBody body = client.newCall(req).execute().body();
        assert(body != null);
        Gson gson = new Gson();
        String strBody = body.string();
        System.out.println(strBody);
        return gson.fromJson(strBody, classObj);
    }

    @Override
    public void run() {
        String gradesUri = System.getenv("GRADES_URI");
        if(gradesUri == null){
            gradesUri = "http://localhost:8081";
        }

        String studentsUri = System.getenv("STUDENTS_URI");
        if(studentsUri == null){
            studentsUri = "http://localhost:8080";
        }

        try{
            StudentsResponse allStudents = this.fetchData(studentsUri, StudentsResponse.class);
            GradesResponse allGrades = this.fetchData(gradesUri, GradesResponse.class);

            final List<ReportCard> cards = Arrays.stream(allStudents.result).map(student -> {
                List<Grade> studentGrades = Arrays.stream(allGrades.result).filter(grade -> grade.studentId == student.id).toList();
                return new ReportCard(student.name + " " + student.surname, studentGrades);
            }).toList();

            cards.forEach(card -> {
                System.out.println(card.toString());
            });

        }catch(MalformedURLException e){
            System.out.println("cannot perform task because the url is malformed");
        }catch(IOException e){
            System.out.println("cannot perform http request: ");
            e.printStackTrace();
        }
    }
}