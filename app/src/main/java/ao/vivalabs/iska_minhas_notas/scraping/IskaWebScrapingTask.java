package ao.vivalabs.iska_minhas_notas.scraping;

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
import java.util.Objects;

import ao.vivalabs.iska_minhas_notas.models.HomeModel;
import ao.vivalabs.iska_minhas_notas.models.TableModel;

public class IskaWebScrapingTask {

    public IskaWebScrapingTask(String username, String password, IskaWebScrapingCallback iskaWebScrapingCallback) {

        String cssPathToTable = "div.table-responsive-sm table";

        // Links
        String url = "http://41.218.115.14/";
        String home = "Discentes/discente.aspx";
        String classification = "Discentes/Secretaria/Classificacoes.aspx?s=B3738228BDA70CB1";

        boolean loggedIn = false;
        boolean hasError = false;
        try {

            String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";

            // Create a "session" map here to persists cookies across all requests
            Map<String, String> session;

            Connection.Response response = Jsoup.connect(url + "login.aspx")
                    .userAgent(userAgent)
                    .method(Connection.Method.GET)
                    .execute();

            session = response.cookies();

            // Get inputs necessary for login
            Document loginPage = response.parse();
            String viewState = Objects.requireNonNull(loginPage.getElementById("__VIEWSTATE")).val();
            String eventValidation = Objects.requireNonNull(loginPage.getElementById("__EVENTVALIDATION")).val();
            String viewStateGenerator = Objects.requireNonNull(loginPage.getElementById("__VIEWSTATEGENERATOR")).val();

            // Make the login
            response = Jsoup.connect(url + "login.aspx")
                    .cookies(session)
                    .userAgent(userAgent)
                    .data("__EVENTTARGET", "")
                    .data("__EVENTARGUMENT", "")
                    .data("__VIEWSTATE", viewState)
                    .data("__EVENTVALIDATION", eventValidation)
                    .data("__VIEWSTATEGENERATOR", viewStateGenerator)
                    .data("ctl00$ContentPlaceHolder1$TxtUtilizador", username)
                    .data("ctl00$ContentPlaceHolder1$TxtSenha", password)
                    .data("ctl00$ContentPlaceHolder1$BtnEntrar", "Entrar")
                    .method(Connection.Method.POST)
                    .followRedirects(false)
                    .timeout(0)
                    .execute();

            // Check login state
            if (response.hasHeader("Location")) {

                loggedIn = true;

                // update session after login
                session.putAll(response.cookies());

                //get user status information
                // ##########################################

                Document doc = Jsoup.connect(url + home)
                        .cookie("ASP.NET_SessionId", Objects.requireNonNull(session.get("ASP.NET_SessionId")))
                        .followRedirects(false)
                        .userAgent(userAgent)
                        .get();

                HomeModel homeModel = new HomeModel(
                        Objects.requireNonNull(doc.getElementById("ContentPlaceHolder1_LblNomeEstudante")).text(),
                        Objects.requireNonNull(doc.getElementById("ContentPlaceHolder1_LblNumeroAluno")).text(),
                        Objects.requireNonNull(doc.getElementById("ContentPlaceHolder1_LblCurso")).text(),
                        Objects.requireNonNull(doc.getElementById("ContentPlaceHolder1_LblTesouraria")).text(),
                        Objects.requireNonNull(doc.getElementById("ContentPlaceHolder1_lblAcademica")).text(),
                        Objects.requireNonNull(doc.getElementById("ContentPlaceHolder1_lblMatricula")).text(),
                        Objects.requireNonNull(doc.getElementById("ContentPlaceHolder1_lblLectivo")).text(),
                        Objects.requireNonNull(doc.getElementById("ContentPlaceHolder1_lblTelefone")).text(),
                        Objects.requireNonNull(doc.getElementById("ContentPlaceHolder1_lblTelefone2")).text(),
                        Objects.requireNonNull(doc.getElementById("ContentPlaceHolder1_lblEmail")).text()
                );

                // ##########################################
                // get user classification
                doc = Jsoup.connect(url + classification)
                        .cookie("ASP.NET_SessionId", Objects.requireNonNull(session.get("ASP.NET_SessionId")))
                        .followRedirects(false)
                        .userAgent(userAgent)
                        .get();

                // get tables
                Elements classificacaoTables = doc.select(cssPathToTable);

                // get table header
                Element tableHeader = classificacaoTables.select("tr").first();

                // save cells to hasMap
                List<TableModel> tablesMapList = new ArrayList<>();
                int totalElements = classificacaoTables.size();
                int id = 0;
                for (Element table : classificacaoTables.select("tbody")) {
                    Elements tempRows = table.select("tr");
                    for (int i = 0; i < tempRows.size(); i++) {
                        Map<String, String> tableMap = new HashMap<>();
                        Elements tempCols = tempRows.get(i).select("td");
                        for (int j = 0; j < tempCols.size(); j++) {
                            assert tableHeader != null;
                            tableMap.put(tableHeader.child(j).text(), tempCols.get(j).text());
                        }
                        id++;
                        tablesMapList.add(new TableModel(
                                id,
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
                                tableMap.get("Melhoria"),
                                totalElements));
                    }

                    totalElements--;

                }
                IskaWebScraping.getInstance().SetIskaWebScraping(homeModel, tablesMapList);
            }

        } catch (IOException e){
            e.printStackTrace();
            hasError = true;
        }

        iskaWebScrapingCallback.postExecute(loggedIn, hasError);

    }
}