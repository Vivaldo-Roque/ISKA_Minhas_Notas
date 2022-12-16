package ao.vivalabs.iska_minhas_notas.models;

public class HomeModel {
    private String nomeEstudante;
    private String numeroAluno;
    private String curso;
    private String tesouraria;
    private String academica;
    private String matricula;
    private String lectivo;
    private String telefone;
    private String telefone2;
    private String email;

    public HomeModel(String nomeEstudante,
    String numeroAluno,
     String curso,
     String tesouraria,
     String academica,
     String matricula,
     String lectivo,
     String telefone,
     String telefone2,
     String email){
        this.nomeEstudante = nomeEstudante;
        this.numeroAluno = numeroAluno;
        this.curso = curso;
        this.tesouraria = tesouraria;
        this.academica = academica;
        this.matricula = matricula;
        this.lectivo = lectivo;
        this.telefone = telefone;
        this.telefone2 = telefone2;
        this.email = email;
    }

    public String getNomeEstudante() {
        return nomeEstudante;
    }

    public void setNomeEstudante(String nomeEstudante) {
        this.nomeEstudante = nomeEstudante;
    }

    public String getNumeroAluno() {
        return numeroAluno;
    }

    public void setNumeroAluno(String numeroAluno) {
        this.numeroAluno = numeroAluno;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getTesouraria() {
        return tesouraria;
    }

    public void setTesouraria(String tesouraria) {
        this.tesouraria = tesouraria;
    }

    public String getAcademica() {
        return academica;
    }

    public void setAcademica(String academica) {
        this.academica = academica;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getLectivo() {
        return lectivo;
    }

    public void setLectivo(String lectivo) {
        this.lectivo = lectivo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTelefone2() {
        return telefone2;
    }

    public void setTelefone2(String telefone2) {
        this.telefone2 = telefone2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
