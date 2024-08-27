package org.example;

public class ResultadoVerificacao {

    private Integer total;
    private boolean umCampo;
    private Integer posicaoAtual;

    public ResultadoVerificacao() {
    }

    public ResultadoVerificacao(Integer total, boolean umCampo) {
        this.total = total;
        this.umCampo = umCampo;
    }

    public ResultadoVerificacao(Integer total, boolean umCampo, Integer posicaoAtual) {
        this.total = total;
        this.umCampo = umCampo;
        this.posicaoAtual = posicaoAtual;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public boolean isUmCampo() {
        return umCampo;
    }

    public void setUmCampo(boolean umCampo) {
        this.umCampo = umCampo;
    }

    public Integer getPosicaoAtual() {
        return posicaoAtual;
    }

    public void setPosicaoAtual(Integer posicaoAtual) {
        this.posicaoAtual = posicaoAtual;
    }
}
