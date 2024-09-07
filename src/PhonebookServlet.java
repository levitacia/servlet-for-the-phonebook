package com.example.demo1;

import java.io.*;
import java.util.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "phonebookServlet", value = "/phonebook/*")
public class PhonebookServlet extends HttpServlet {
    private Map<String, ArrayList<String>> phonebook;
    private final Object lock = new Object();  // Объект для синхронизации

    @Override
    public void init() {
        phonebook = new HashMap<>();
        loadFromFile();
    }

    private void loadFromFile () {
        String filePath = "C:/Users/1/IdeaProjects/demo1/src/main/webapp/WEB-INF/phonebook.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    String name = parts[0];
                    String phonesPart = parts[1];
                    String[] phones = phonesPart.split(";");
                    ArrayList<String> phoneList = new ArrayList<>(Arrays.asList(phones));
                    phonebook.put(name, phoneList);
                } else {
                    System.err.println("Invalid format in line: " + line);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToFile() {
        String filePath = "C:/Users/1/IdeaProjects/demo1/src/main/webapp/WEB-INF/phonebook.txt";
        synchronized (lock) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (Map.Entry<String, ArrayList<String>> entry : phonebook.entrySet()) {
                    String name = entry.getKey();
                    ArrayList<String> phones = entry.getValue();
                    String phonesStr = String.join(";", phones);

                    writer.write(name + "," + phonesStr);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<head><title>Phonebook</title></head>");


        String pathInfo = request.getPathInfo();
        if ("/add".equals(pathInfo)) {
            String name = request.getParameter("name");
            String phone = request.getParameter("phone");

            if (name != null && !name.trim().isEmpty() && phone != null && !phone.trim().isEmpty()) {
                synchronized (lock) {
                    if (phonebook.containsKey(name)) {
                        ArrayList<String> phones = phonebook.get(name);
                        phones.add(phone);
                    } else {
                        ArrayList<String> phones = new ArrayList<>();
                        phones.add(phone);
                        phonebook.put(name, phones);
                    }
                }
            }
        }
        else if ("/reset".equals(pathInfo)) {
            synchronized (lock) {
                phonebook.clear();
                String filePath = "C:/Users/1/IdeaProjects/demo1/src/main/webapp/WEB-INF/phonebook.txt";

                try (FileWriter fileWriter = new FileWriter(filePath)) {

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if ("/save".equals(pathInfo)) {
            synchronized (lock) {
                saveToFile();
            }
        }

        out.println("<h1>Phonebook</h1>");
        out.println("<p>Last request URI was: " + request.getRequestURI() + "</p>");
        out.println(getMainPage());
        out.println("</html></body>");
    }

    public String getMainPage() {
        StringBuilder sb = new StringBuilder();
        synchronized (lock) {  
            for (Map.Entry<String, ArrayList<String>> entry : phonebook.entrySet()) {
                sb.append("<p>").append(entry.getKey()).append("  ").append(entry.getValue()).append("</p>\n");
            }
        }

        sb.append("<form method=\"GET\" action=\"/demo1_war_exploded/phonebook/add\">\n");
        sb.append("Name: <input type=\"text\" name=\"name\">\n");
        sb.append("Phone: <input type=\"text\" name=\"phone\">\n");
        sb.append("<input type=\"submit\" value=\"add\">\n");
        sb.append("</form>");

        sb.append("<a href=\"/demo1_war_exploded/phonebook/save\">save</a><br><br>");
        sb.append("<a href=\"/demo1_war_exploded/phonebook/reset\">reset</a><br><br>");
        sb.append("<a href=\"/demo1_war_exploded\"><h3>Back</h3></a>");
        return sb.toString();
    }

    public void destroy() {
    }
}