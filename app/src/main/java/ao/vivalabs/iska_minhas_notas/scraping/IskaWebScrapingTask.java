package ao.vivalabs.iska_minhas_notas.scraping;

import android.os.AsyncTask;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ao.vivalabs.iska_minhas_notas.models.ClassTableModel;
import ao.vivalabs.iska_minhas_notas.models.HomeModel;

public class IskaWebScrapingTask extends AsyncTask<Void, Void, Void> {

    private final List<ClassTableModel> tablesMapList = new ArrayList<>();
    private boolean loggedin = false;
    private boolean hasError = false;
    private final String usermane;
    private final String password;
    private final IskaWebScrapingCallback iskaWebScrapingCallback;

    public IskaWebScrapingTask(String username, String password, IskaWebScrapingCallback iskaWebScrapingCallback){
        this.usermane = username;
        this.password = password;
        this.iskaWebScrapingCallback = iskaWebScrapingCallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        iskaWebScrapingCallback.preExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        String cssPathToTable = "div.table-responsive-sm table";

        // Links
        String url = "http://41.218.115.14/";
        String home = "Discentes/discente.aspx";
        String classification = "Discentes/Secretaria/Classificacoes.aspx?s=B3738228BDA70CB1";

        try{

            String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";

            // Create a "session" map here to persists cookies across all requests
            Map<String, String> session;

            Connection.Response response = Jsoup.connect(url+"login.aspx")
                    .userAgent(userAgent)
                    .method(Connection.Method.GET)
                    .execute();

            session = response.cookies();

            // Get inputs necessary for login
            Document loginPage = response.parse();
            String viewState = loginPage.getElementById("__VIEWSTATE").val();
            String eventValidation = loginPage.getElementById("__EVENTVALIDATION").val();
            String viewStateGenerator = loginPage.getElementById("__VIEWSTATEGENERATOR").val();

            // Make the login
            response = Jsoup.connect(url+"login.aspx")
                    .cookies(session)
                    .userAgent(userAgent)
                    .data("__EVENTTARGET", "")
                    .data("__EVENTARGUMENT","")
                    .data("__VIEWSTATE", viewState)
                    .data("__EVENTVALIDATION", eventValidation)
                    .data("__VIEWSTATEGENERATOR", viewStateGenerator)
                    .data("ctl00$ContentPlaceHolder1$TxtUtilizador", usermane)
                    .data("ctl00$ContentPlaceHolder1$TxtSenha", password)
                    .data("ctl00$ContentPlaceHolder1$BtnEntrar", "Entrar")
                    .method(Connection.Method.POST)
                    .followRedirects(false)
                    .timeout(0)
                    .execute();

            // Check login state
            if(response.hasHeader("Location")){

                loggedin = true;

                // update session after login
                session.putAll(response.cookies());

                //get user status information
                // ##########################################

                Document doc = Jsoup.connect(url+home)
                        .cookie("ASP.NET_SessionId", session.get("ASP.NET_SessionId"))
                        .followRedirects(false)
                        .userAgent(userAgent)
                        .get();

                HomeModel homeModel = new HomeModel(
                        doc.getElementById("ContentPlaceHolder1_LblNomeEstudante").text(),
                        doc.getElementById("ContentPlaceHolder1_LblNumeroAluno").text(),
                        doc.getElementById("ContentPlaceHolder1_LblCurso").text(),
                        doc.getElementById("ContentPlaceHolder1_LblTesouraria").text(),
                        doc.getElementById("ContentPlaceHolder1_lblAcademica").text(),
                        doc.getElementById("ContentPlaceHolder1_lblMatricula").text(),
                        doc.getElementById("ContentPlaceHolder1_lblLectivo").text(),
                        doc.getElementById("ContentPlaceHolder1_lblTelefone").text(),
                        doc.getElementById("ContentPlaceHolder1_lblTelefone2").text(),
                        doc.getElementById("ContentPlaceHolder1_lblEmail").text()
                );

                // ##########################################
                // get user classification
                doc = Jsoup.connect(url+classification)
                        .cookie("ASP.NET_SessionId", session.get("ASP.NET_SessionId"))
                        .followRedirects(false)
                        .userAgent(userAgent)
                        .get();

                // get tables
                Elements classificacaoTable = doc.select(cssPathToTable);

                // get table headers
                Element tableHeader = classificacaoTable.select("tr").first();

                // save cells to hasMap
                for(Element table: classificacaoTable.select("tbody")){
                    Elements tempRows = table.select("tr");
                    for(int i = 0; i < tempRows.size(); i++){
                        Map<String, String> tableMap = new HashMap<>();
                        Elements tempCols = tempRows.get(i).select("td");
                        for(int j = 0; j < tempCols.size(); j++){
                            tableMap.put(tableHeader.child(j).text(), tempCols.get(j).text());
                        }

                        tablesMapList.add(new ClassTableModel(
                                tableMap.get("Disciplina"),
                                tableMap.get("Abrev."),
                                tableMap.get("Ano"),
                                tableMap.get("Turma"),
                                tableMap.get("Tipo"),
                                tableMap.get("Nota Final"),
                                tableMap.get("A. C."),
                                tableMap.get("1ª P."),
                                tableMap.get("2ª P."),
                                tableMap.get("Final Contínua"),
                                tableMap.get("Resultado"),
                                tableMap.get("Exame"),
                                tableMap.get("Recurso"),
                                tableMap.get("Ép. Espec."),
                                tableMap.get("Melhoria")
                        ));
                    }
                }
                IskaWebScraping.getInstance().SetIskaWebScraping(homeModel, tablesMapList);
            } else{
                loggedin = false;
            }

        } catch (IOException e){
            e.printStackTrace();
            hasError = true;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        iskaWebScrapingCallback.postExecute(loggedin, hasError);
    }
}