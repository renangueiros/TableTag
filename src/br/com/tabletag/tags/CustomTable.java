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
public class CustomTable extends SimpleTagSupport {

    // Lista com os cabeçalhos dos campos que devem aparecer na tabela
    private List customHead;
    // Lista com todos os dados
    private List bodyList;
    // Lista com todos os dados do cabeçalho
    private List fullHead;
    // Array (String[]) com os campos que devem aparecer na tabela
    private String[] presentedOnTable;
    // Id (html) da tabela
    private String idTable;
    // Classe (html) da tabela
    private String classTable;
    // Calsse (html) do cabeçalho da tabela
    private String classHead;
    // Classe (html) da corpor da tabela
    private String classBody;

    /**
     * Called by the container to invoke this tag. The implementation of this
     * method is provided by the tag library developer, and handles all tag
     * processing, body iteration, etc.
     *
     * @throws javax.servlet.jsp.JspException
     */
    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();

        try {
            // Escreve a estilização da tabela
            out.println(setStyle());
            out.println("<table" + getIdTable() + getClassTable() + ">");

            // Apenas escrever o cabeçalho se a lista de cabeçalho tiver itens
            if (customHead != null) {
                // método auxiliar para criação do cabeçalho da tabela
                writeHeadTable(customHead, out);
            }

            // Metódo auxiliar pata criação do corpo da tabela
            writeBodyTable(bodyList, presentedOnTable, out);

            out.println("</table>");

            // Cria as ciaxas de dialogo com todas as informações da tabela
            createDialogBox(bodyList, out);
        } catch (java.io.IOException ex) {
            throw new JspException("Error in CustomTable tag", ex);
        }
    }

    public void setCustomHead(List customHead) {
        this.customHead = customHead;
    }

    public void setBodyList(List bodyList) {
        this.bodyList = bodyList;
    }

    public void setFullHead(List fullHead) {
        this.fullHead = fullHead;
    }

    public void setPresentedOnTable(String[] presentedOnTable) {
        this.presentedOnTable = presentedOnTable;
    }

    public void setIdTable(String idTable) {
        this.idTable = idTable;
    }

    public String getIdTable() {
        if (idTable != null) {
            return " id='" + this.idTable + "' ";
        } else {
            return "";
        }
    }

    public void setClassTable(String classTable) {
        this.classTable = classTable;
    }

    public String getClassTable() {
        if (classTable != null) {
            return " class='" + this.classTable + "' ";
        } else {
            return "";
        }
    }

    public void setClassHead(String classHead) {
        this.classHead = classHead;
    }

    public String getClassHead() {
        if (classTable != null) {
            return " class='" + this.classHead + "'";
        } else {
            return "";
        }
    }

    public void setClassBody(String classBody) {
        this.classBody = classBody;
    }

    public String getClassBody() {
        if (classBody != null) {
            return " class='" + this.classBody + "'";
        } else {
            return "";
        }
    }

    /**
     * Método para criação do cabeçalho da tabela. Recebe a lista de cabeçalhos
     * (java.uitl.List) e o escritor da página (javax.servlet.jsp.JspWriter).
     */
    private void writeHeadTable(List headList, JspWriter out) throws IOException {
        out.println("<thead " + getClassHead() + "> <tr>");

        // Percorre todos os itens da lista
        for (int i = 0; i < headList.size(); i++) {
            // Escreve cada item da lista na página (html).
            out.println("<th>" + headList.get(i).toString() + "</th>");

            // Escreve somente se for o ultimo campo da linha do cabeçalho
            if (i == presentedOnTable.length - 1) {
                out.println("<th>Visualizar</th>");
            }
        }

        out.println("</tr> </thead>");
    }

    /**
     * Método para criação do corpo da tabela. Recebe a lista de
     * dados(java.util.List), a array(java.lang.String[]) com os dados que se
     * dedeja realmente mostrar na tabela e escritor da página
     * (javax.servlet.jsp.JspWriter).
     */
    private void writeBodyTable(List bodyList, String[] presentedOnTable, JspWriter out) throws IOException {
        out.println("<tbody" + getClassBody() + ">");

        // Percorre todos os itens da lista
        for (int i = 0; i < bodyList.size(); i++) {
            out.println("<tr>");

            // Percorre todos os campos declarados na classe descritora da lista de dados.
            for (Field field : bodyList.get(i).getClass().getDeclaredFields()) {

                // Configura todos os campos como acessíveis, mesmo se eles forem declarados com private
                field.setAccessible(true);

                // Percorre todos os campos do array presentendOnTable
                for (int y = 0; y < presentedOnTable.length; y++) {
                    // somente escreve o campo se ele for igual a algum elemento do array
                    if (field.getName().equals(presentedOnTable[y])) {
                        // escreve cada campo na tabela.
                        try {
                            out.println("<td>" + field.get(bodyList.get(i)).toString() + "</td>");

                            // Escreve somente se for o ultimo campo da linha dos dados
                            if (y == presentedOnTable.length - 1) {
                                out.println("<td> <a href='#openModal" + i + "'>mais informações</a></td>");
                            }
                        } catch (IllegalArgumentException | IllegalAccessException ex) {
                            Logger.getLogger(SimpleTable.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

            }
            out.println("</tr>");
        }

        out.println("</tbody>");
    }

    /**
     * Método auxiliar para estilização da tabela. Retorna texto (String) com
     * todo o estilo (css).
     */
    private String setStyle() {
        return "<style>"
                + "table {\n"
                + "    background-color: #FFFFFF;\n"
                + "    border: solid 1px #666666;\n"
                + "    border-collapse: collapse;\n"
                + "}\n"
                + "\n"
                + "\n"
                + "table tbody tr:nth-child(even) {\n"
                + "    background-color: #f2f2f2;\n"
                + "}\n"
                + "\n"
                + "table th {\n"
                + "    background-color: #2B2B2B;\n"
                + "    border: solid 1px #666666;\n"
                + "    color: #FFFFFF;\n"
                + "    padding: 8px;\n"
                + "}\n"
                + "\n"
                + "table td {\n"
                + "    border: solid 1px #666666;\n"
                + "    padding: 8px;\n"
                + "}"
                + "</style>";
    }

    /**
     * Método Auxiliar para criação de todas as caixas de dialogo, que irão
     * conter todas as informações da tabela. Recebe "bodyList" (List) contendo
     * todas as informações da tabela
     *
     */
    private void createDialogBox(List bodyList, JspWriter out) throws IOException {

        // escreve a estilização (css) que as caixas de dialogo modais devem ter
        out.println("<style>"
                + ".modalDialog {\n"
                + "    bottom: 0;\n"
                + "    display: table;\n"
                + "    height: 100%;\n"
                + "    left: 0;\n"
                + "    opacity:0;\n"
                + "    pointer-events: none;\n"
                + "    position: fixed;\n"
                + "    right: 0;\n"
                + "    text-align: center;\n"
                + "    top: 0;\n"
                + "    transition: opacity 400ms ease-in;\n"
                + "    z-index: 10;\n"
                + "    width: 100%;\n"
                + "}\n"
                + "\n"
                + ".modalDialog:target {\n"
                + "    opacity: 1;\n"
                + "    pointer-events: auto;\n"
                + "}\n"
                + "\n"
                + ".contentModal {\n"
                + "    background-color: #FFFFFF;\n"
                + "    display: inline-block;\n"
                + "    margin: 0 auto;\n"
                + "    padding: 20px;\n"
                + "}\n"
                + "\n"
                + ".cellModal {\n"
                + "    background-color: rgba(0, 0, 0, 0.8);\n"
                + "    display: table-cell;\n"
                + "    vertical-align: middle;\n"
                + "}\n"
                + "\n"
                + ".close { \n"
                + "    background: #2B2B2B;\n"
                + "    border: solid 1px #FFFFFF;\n"
                + "    border-radius: 12px;\n"
                + "    color: #FFFFFF;\n"
                + "    float: right;\n"
                + "    font-weight: bold;\n"
                + "    line-height: 24px;\n"
                + "    position: relative;\n"
                + "    text-align: center;\n"
                + "    text-decoration: none; \n"
                + "    top: -34px;\n"
                + "    width: 24px;\n"
                + "}\n"
                + "\n"
                + ".tableContent {\n"
                + "    clear: both;\n"
                + "}\n"
                + "\n"
                + "</style>");

        // Varrer todos itens da lista
        for (int i = 0; i < bodyList.size(); i++) {
            // Escreve o modal
            out.println("<div id='openModal" + i + "' class='modalDialog'>"
                    + "<div class='cellModal'>"
                    + "<div class='contentModal'>"
                    + "    <a href='#close' class='close'>X</a>"
                    + "        <div class='tableContent'>");

            // Escreve as informações de um item da lista
            showMoreInformation(i, out);

            out.println("</div>"
                    + "</div>"
                    + "</div>"
                    + "</div>");
        }
    }

    /**
     * Método para escrever uma tabela com todas as informações de um único
     * item. Recebe o índice (int) sobre qual item se quer escrever.
     *
     */
    private void showMoreInformation(int index, JspWriter out) throws IOException {

        // Instancia um novo objeto com as informações de um item pegado da bodyList.
        Object item = bodyList.get(index);
        // Instancia todos os campos declarados na classe desse item.
        Field[] fields = item.getClass().getDeclaredFields();

        out.println("<table>");

        // Varre o array com todos os campos (Field) do item (Object)
        for (int i = 0; i < fields.length; i++) {
            try {
                // Configura todos os campos como acessíveis, mesmo se eles forem declarados com private
                fields[i].setAccessible(true);

                out.println("<tr>");
                // Escreve cada item do cabeçalho se ele não for nulo
                if (fullHead != null) {
                    out.println("<th>" + fullHead.get(i).toString() + "</th>");
                }
                // Escreve o valor guardade em cada campo do item.
                out.println("<td>" + fields[i].get(item).toString() + "</td>");
                out.println("</tr>");
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(CustomTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        out.println("</table>");
    }
}
