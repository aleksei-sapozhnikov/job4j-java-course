package ru.job4j.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class EchoServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html");
        PrintWriter writer = new PrintWriter(res.getOutputStream());
        writer.append("<center><h1>Hello world!</h1></center><br>");
        writer.append("<div align='center'><h2>I'm trying to do some servlets...</h2></div>");
        writer.append("<div align='right'><h4>It's so goddamn hard...</h4></div>");
        writer.flush();
    }
}