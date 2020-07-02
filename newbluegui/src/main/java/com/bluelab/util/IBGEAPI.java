package com.bluelab.util;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;


public class IBGEAPI {

    public class Uf {

        private int id;
        private String nome;
        private String sigla;

        public int getId() {
            return id;
        }

        public String getNome() {
            return nome;
        }

        public String getSigla() {
            return sigla;
        }
    }

    public class Cidade {

        private int id;
        private String nome;

        public int getId() {
            return id;
        }

        public String getNome() {
            return nome;
        }

    }

    private String UF_API = "https://servicodados.ibge.gov.br/api/v1/localidades/estados?orderBy=nome";

    private Uf[] ufs;
    private Cidade[] cidades;

    public IBGEAPI() {
        HttpURLConnection con = null;
        try {
            URL url = new URL(UF_API);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            switch (con.getResponseCode()) {
            case 200:
                String json = getJson(url);

                Gson gson = new Gson();

                ufs = (gson.fromJson(json, Uf[].class));

                break;
            case 500:
                System.out.println("Status 500");
                break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (con != null)
                con.disconnect();
        }
    }

    public static String getJson(URL url) {
        if (url == null)
            throw new RuntimeException("URL é null");

        String html = null;
        StringBuilder sB = new StringBuilder();
        try (BufferedReader bR = new BufferedReader(new InputStreamReader(url.openStream()))) {
            while ((html = bR.readLine()) != null) {
                sB.append(html);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return sB.toString();
    }

    public List<String> getUfs() {
        List<String> listUf = new ArrayList<String>();

        for (Uf uf : ufs) {
            listUf.add(uf.getSigla());
        }

        return listUf;
    }

    public List<String> getCidades(String uf) {
        HttpURLConnection con = null;
        
        String CIDADE_API = "https://servicodados.ibge.gov.br/api/v1/localidades/estados/{UF}/municipios";
        CIDADE_API = CIDADE_API.replace("{UF}", uf);
        
        try {
            URL url = new URL(CIDADE_API);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            switch (con.getResponseCode()) {
            case 200:
                String json = getJson(url);

                Gson gson = new Gson();

                cidades = (gson.fromJson(json, Cidade[].class));

                break;
            case 500:
                System.out.println("Status 500");
                break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (con != null)
                con.disconnect();
        }

        List<String> listCidade = new ArrayList<String>();

        for (Cidade cidade : cidades) {
            listCidade.add(cidade.getNome());
        }

        return listCidade;
    }
}
