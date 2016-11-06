/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.tabletag.tags;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author Renan
 */
public class SimpleTable extends SimpleTagSupport {

    // Lista de Cabeçalhos.
    private List headList;
    // Lista de dados para o corpo da tabela
    private List bodyList;

    /**
     * Called by the container to invoke this tag. The implementation of this
     * method is provided by the tag library developer, and handles all tag
     * processing, body iteration, etc.
     * @throws javax.servlet.jsp.JspException
     */
    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();

        try {
            // Escreve a estilização da tabela
            out.println(setStyle());
            out.println("<table>");

            // Apenas escrever o cabeçalho se a lista de cabeçalho tiver itens
            if (headList != null) {
                // método auxiliar para criação do cabeçalho da tabela
                writeHeadTable(headList, out);
            }

            // Metódo auxiliar pata criação do corpo da tabela
            writeBodyTable(bodyList, out);

            out.println("</table>");
        } catch (java.io.IOException ex) {
            throw new JspException("Error in SimpleTable tag", ex);
        }
    }

    public void setHeadList(List headList) {
        this.headList = headList;
    }

    public void setBodyList(List bodyList) {
        this.bodyList = bodyList;
    }

    /**
     * Método para criação do cabeçalho da tabela.
     * Recebe a lista de cabeçalhos (java.uitl.List) e o escritor da página (javax.servlet.jsp.JspWriter).
     */
    private void writeHeadTable(List headList, JspWriter out) throws IOException {
        out.println("<thead> <tr>");

        // Percorre todos os itens da lista
        for (int i = 0; i < headList.size(); i++) {
            // Escreve cada item da lista na página (html).
            out.println("<th>" + headList.get(i).toString() + "</th>");
        }

        out.println("</tr> </thead>");
    }

    /**
     * Método para criação do corpo da tabela.
     * Recebe a lista de dados (java.uitl.List) e o escritor da página (javax.servlet.jsp.JspWriter).
     */
    private void writeBodyTable(List bodyList, JspWriter out) throws IOException {
        out.println("<tbody>");

        // Percorre todos os itens da lista
        for (int i = 0; i < bodyList.size(); i++) {
            out.println("<tr>");

            // Percorre todos os campos declarados na classe descritora da lista de dados.
            for (Field field : bodyList.get(i).getClass().getDeclaredFields()) {

                // Confugura todos os campo como acessíveis, mesmo se eles forem declarados com private
                field.setAccessible(true);

                // escreve cada campo na tabela.
                try {
                    out.println("<td>" + field.get(bodyList.get(i)).toString() + "</td>");
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(SimpleTable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            out.println("</tr>");
        }

        out.println("</tbody>");
    }

    /**
     * Método auxiliar para estilização da tabela.
     * Retorna texto (String) com todo o estilo (css).
     */
    private String setStyle() {
        return "<style>"
                + "table {\n"
                + "    background-color: #FFFFFF;\n"
                + "    border: solid 1px #666666;\n"
                + "    border-collapse: collapse;\n"
                + "}\n"
                + "\n"
                + "table thead tr {\n"
                + "    background-color: #2B2B2B;\n"
                + "    color: #FFFFFF;\n"
                + "}\n"
                + "\n"
                + "table tbody tr:nth-child(even) {\n"
                + "    background-color: #f2f2f2;\n"
                + "}\n"
                + "\n"
                + "table th {\n"
                + "    border: solid 1px #666666;\n"
                + "    padding: 8px;\n"
                + "}\n"
                + "\n"
                + "table td {\n"
                + "    border: solid 1px #666666;\n"
                + "    padding: 8px;\n"
                + "}"
                + "</style>";
    }

}
