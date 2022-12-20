package ao.vivalabs.iska_minhas_notas.models;

public class HomeModel {
    private final String nomeEstudante;
    private final String numeroAluno;
    private final String curso;
    private final String tesouraria;
    private final String academica;
    private final String matricula;
    private final String lectivo;
    private final String telefone;
    private final String telefone2;
    private final String email;

    public HomeModel(String nomeEstudante,
                     String numeroAluno,
                     String curso,
                     String tesouraria,
                     String academica,
                     String matricula,
                     String lectivo,
                     String telefone,
                     String telefone2,
                     String email) {
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

    public String getNumeroAluno() {
        return numeroAluno;
    }

    public String getCurso() {
        return curso;
    }

    public String getTesouraria() {
        return tesouraria;
    }

    public String getAcademica() {
        return academica;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getLectivo() {
        return lectivo;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getTelefone2() {
        return telefone2;
    }

    public String getEmail() {
        return email;
    }
}
