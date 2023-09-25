package clone.webFramework.servlet;

public class SamplePage {
    public String error404 = "";
    public String welcome = "";
    public int errorLen = 0;
    public int welcomeLen = 0;
    public byte[] errorByte;
    public byte[] welcomeByte;

    SamplePage(){
        StringBuilder errorSB = new StringBuilder();
        errorSB.append("<!DOCTYPE html>");
        errorSB.append("<html>");
        errorSB.append("   <head>");
        errorSB.append("       <meta charset=\"UTF-8\">");
        errorSB.append("       <meta name=\"author\" content=\"Dochi\">");
        errorSB.append("       <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        errorSB.append("       <title>Example</title>");
        errorSB.append("   </head>");
        errorSB.append("   <body>");
        errorSB.append("       <h5>Hello, HttpServer!!!</h5>");
        errorSB.append("       <h5>Error Page</h5>");
        errorSB.append("   </body>");
        errorSB.append("</html>");
        error404 = errorSB.toString();
        errorLen = error404.length();
        errorByte = new byte[errorLen];


        StringBuilder welcomeSB = new StringBuilder();
        welcomeSB.append("<!DOCTYPE html>");
        welcomeSB.append("<html>");
        welcomeSB.append("   <head>");
        welcomeSB.append("       <meta charset=\"UTF-8\">");
        welcomeSB.append("       <meta name=\"author\" content=\"Dochi\">");
        welcomeSB.append("       <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        welcomeSB.append("       <title>Example</title>");
        welcomeSB.append("   </head>");
        welcomeSB.append("   <body>");
        welcomeSB.append("       <h5>Hello, HttpServer!!!</h5>");
        welcomeSB.append("   </body>");
        welcomeSB.append("</html>");
        welcome = welcomeSB.toString();
        welcomeLen = welcome.length();
    }

    public String errorMethod(){
        return error404;
    }
    public String welcomeMethod() {return welcome;}
}
