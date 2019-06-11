/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apps.controleur;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.Part;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Absolute
 */
public class Controleur extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    String file = "";
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action.equals("/store-picture")){
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            try {
                if(upload(request))out.println("<center>Upload finished successfully</center>"
                            + "</span><br/>"
                            + "<a href=\"/system/download/file?ref="+file+"\" >Download file</a>");
                else {
                    out.println("<span style=\"color:red\"><center>this file has not taked as a passenger ou fare</center>");
                }
                
                out.println("<script>window.top.window.displaywait()</script>");
            } finally {            
                out.close();
            }
        }
        if (action.equals("/ressource")){
            String ref = request.getParameter("ref");
            String type = request.getParameter("type");
            if((type!=null)&&!type.isEmpty()){
                if(type.equalsIgnoreCase("css")){
                    response.sendRedirect("/css/"+ref);
                    return;
                }else if(type.equalsIgnoreCase("script")){
                    response.sendRedirect("/js/"+ref);
                    return;
                }else if(type.equalsIgnoreCase("image")){
                    response.sendRedirect("/image/"+ref);
                    return;
                }
            }
            response.sendRedirect("");
            return;
        }
        
        if (action.equals("/download/file")){
            String ref = request.getParameter("ref");
            response.sendRedirect("/ressources/"+ref);
            return;
        }
    }
    
    public boolean upload(HttpServletRequest request) throws ServletException, IOException{
        
        /** crée un nouveau multipart parser, la taille des objets parsés étant de 10 MB au maximum **/  
        MultipartParser mp = new MultipartParser(request, 720*1024*1024); // 10MB
        Part part;
        int i=0;
        /** itére tous les parties **/
        while ((part = mp.readNextPart()) != null) {
        /** s'il s'agit du fichier **/
            if (part.getName().equals("images")) {
    
        /** recupere les données sous la forme d'un FilePart **/
                FilePart filePart = (FilePart) part;
                String fileName = filePart.getFileName();
        /** recopie dans un nouveau fichier **/
                //if(extension(fileName).isEmpty()) return false;
                
                if (fileName != null) {
                    file = "file_"+i+"_"+System.currentTimeMillis();
                    long size = filePart.writeTo(new File(request.getRealPath("")+"/ressources/"+file));
                    
                    break;
                }
            /** s'il s'agit de la zone de texte **/
            } 
        }
        return true;
    }
    
    private String extension(String file){
        if(file==null) return "";
        if(file.isEmpty()) return "";
        String []allowedTypes = {"sql","ear","war","class","c","java","txt","nrg","iso","tar","rar","zip","pdf","pptx","ppt","docx",
                                "doc","mp4","ogg","flv","rmvb","wmv","avi","png", "jpg", "jpeg", "gif"};
        for(int i=0;i!=allowedTypes.length;i++){
            if(file.toLowerCase().contains("."+allowedTypes[i].toLowerCase())) return allowedTypes[i];
        }
        return "";
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
