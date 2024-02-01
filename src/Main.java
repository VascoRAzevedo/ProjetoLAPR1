
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.io.FileWriter;
import java.io.IOException;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.String.valueOf;


public class Main {

    public static Scanner in = new Scanner(System.in);
    public static Runtime rt = Runtime.getRuntime();

    //DECLARAR CONSTANTES
    public static final int MIN_PASSO = 0; //não esquecer de não incluir o 0
    public static final int MAX_PASSO = 1;//perguntar se é melhor fazer mais do que um método
    public static final int MIN_DIAS = 0;

    //FIM DECLARAÇÃO CONSTANTES


    //declarar ficheiro a ser utilizado para leitura.
    public static File parametrosFile;
    public static File valIniciaisFile;


    //main method
    public static void main(String[] args) throws IOException {
        int escolherMetodo = -1, nDias = 0;
        double passoIntegracao = 0;

        if (args.length == 0) {
            //modo interativo
            parametrosFile = new File("parametros.csv");
            valIniciaisFile = new File("val_iniciais.csv");
            int nCasos = contarCasos(parametrosFile);
            double[][] parametros = lerParametros(parametrosFile, nCasos);

            System.out.println();
            System.out.println();
            System.out.println("                        Bem vindo.                          "); //header do programa apenas no início

            while (escolherMetodo != 0) {
                //executar menu;
                escolherMetodo = menu1();

                if (escolherMetodo == 1 || escolherMetodo == 2) {

                    passoIntegracao = lerPasso(MIN_PASSO, MAX_PASSO);
                    nDias = lerDias(MIN_DIAS);

                    for (int caso = 0; caso < nCasos; caso++) {
                        double[] sir = lerValIniciais(valIniciaisFile);
                        double[][] valores;
                        String caminhoArquivo;
                        if (escolherMetodo == 1) {
                            valores = euler(parametros, sir, nDias, passoIntegracao, caso);
                            caminhoArquivo = "FicheirosCSV/matriz_resultado_Euler_caso" + (caso + 1) + ".csv";
                        } else {
                            valores = rk4(parametros, sir, nDias, passoIntegracao, caso);
                            caminhoArquivo = "FicheirosCSV/matriz_resultado_RK4_caso" + (caso + 1) + ".csv";
                        }
                        escreverCSV(valores, caminhoArquivo);
                        escreverMensagemSucesso(caso, escolherMetodo);
                        desenharGrafico(escreverScriptGNU(nDias, escolherMetodo, caso));
                    }
                    escolherMetodo = menu2();
                }
            }

        } else if (args.length == 10) {
            //se for para correr no modo não interativo
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "-b" -> parametrosFile = new File(args[i + 1]);
                    case "-c" -> valIniciaisFile = new File(args[i + 1]);
                    case "-m" -> escolherMetodo = Integer.parseInt(args[i + 1]);
                    case "-p" -> passoIntegracao = Double.parseDouble(args[i + 1]);
                    case "-d" -> nDias = Integer.parseInt(args[i + 1]);
                }
            }

            if ((escolherMetodo == 1 || escolherMetodo == 2) && nDias > 0 && passoIntegracao > 0 && passoIntegracao <= 1) {
                int nCasos = contarCasos(parametrosFile);
                double[][] parametros = lerParametros(parametrosFile, nCasos);

                if (escolherMetodo == 1) {
                    //executar método euler
                    for (int caso = 0; caso < nCasos; caso++) {
                        double[] sir = lerValIniciais(valIniciaisFile);
                        double[][] valores = euler(parametros, sir, nDias, passoIntegracao, caso);
                        String caminhoArquivo = "FicheirosCSV/matriz_resultado_Euler_caso" + (caso + 1) + ".csv";
                        escreverCSV(valores, caminhoArquivo);
                        desenharGrafico(escreverScriptGNU(nDias, escolherMetodo, caso));
                        escreverMensagemSucesso(caso, escolherMetodo);
                    }
                } else {
                    //executar método RK4
                    for (int caso = 0; caso < nCasos; caso++) {
                        double[] sir = lerValIniciais(valIniciaisFile);
                        double[][] valores = rk4(parametros, sir, nDias, passoIntegracao, caso);
                        String caminhoArquivo = "FicheirosCSV/matriz_resultado_RK4_caso" + (caso + 1) + ".csv";
                        escreverCSV(valores, caminhoArquivo);
                        desenharGrafico(escreverScriptGNU(nDias, escolherMetodo, caso));
                        escreverMensagemSucesso(caso, escolherMetodo);
                    }
                }
            } else {
                System.out.println("ERRO: Argumento é inválido");
            }

        } else {
            System.out.println("Erro: o número de argumentos inserido não é suficiente para executar o modo não-interativo");
        }

    }



    public static int contarCasos(File ficheiro) throws FileNotFoundException {
        Scanner ler = new Scanner(ficheiro);
        ler.useDelimiter("\\n");
        int n_casos = -1;//a primeira linha não conta
        while (ler.hasNext()) {
            n_casos++;
            ler.next();
        }
        ler.close();
        return n_casos;
    }

    public static double[][] lerParametros(File ficheiro, int casos) throws FileNotFoundException {
        Scanner ler = new Scanner(ficheiro);
        ler.nextLine();
        int colunas = 8;
        double[][] resultado = new double[casos][colunas];
        for (int i = 0; i < casos; i++) {
            String[] values = ler.nextLine().split(";");
            for (int j = 0; j < colunas; j++) {
                resultado[i][j] = Double.parseDouble(values[j].replace(",", "."));

            }
        }
        return resultado;
    }

    public static double[] lerValIniciais(File ficheiro) throws FileNotFoundException {

        Scanner ler = new Scanner(ficheiro);
        ler.nextLine();

        String[] values = ler.nextLine().split(";");
        double[] resultado = new double[values.length];

        for (int i = 0; i < values.length; i++) {
            resultado[i] = Double.parseDouble(values[i].replace(",", "."));
        }

        ler.close();
        return resultado;
    } //teste feito

    public static int menu1() {
        int num, minimo = 0, maximo = 2;
        System.out.println();
        System.out.println("|----------------------------------------------------------|");
        System.out.println("|                                                          |");
        System.out.println("|           Escolha entre as seguintes opções:             |");
        System.out.println("|                                                          |");
        System.out.println("|      1- Utilizar o método de Euler para o cálculo.       |");
        System.out.println("|   2- Utilizar o método de Runge Kutta para  o cálculo.   |");
        System.out.println("|                  0- Fechar programa.                     |");
        System.out.println("|                                                          |");
        System.out.println("|----------------------------------------------------------|");
        System.out.println();
        System.out.println();

        num = in.nextInt();

        while (num < minimo || num > maximo) {
            System.out.println();
            System.out.println("|----------------------------------------------------------|");
            System.out.println("|                                                          |");
            System.out.println("|                     Opção inválida!                      |");
            System.out.println("|           Escolha entre as seguintes opções:             |");
            System.out.println("|                                                          |");
            System.out.println("|      1- Utilizar o método de Euler para o cálculo.       |");
            System.out.println("|   2- Utilizar o método de Runge Kutta para  o cálculo.   |");
            System.out.println("|                  0- Fechar programa.                     |");
            System.out.println("|                                                          |");
            System.out.println("|----------------------------------------------------------|");
            System.out.println();
            System.out.println();
            num = in.nextInt();
        }

        return num;
    }

    public static double lerPasso(int minimo, int maximo) {
        double input;
        System.out.println();
        System.out.println("|----------------------------------------------------------|");
        System.out.println("|                                                          |");
        System.out.println("|          Insere o tamanho do Passo de Integração:        |");
        System.out.println("|        O valor tem de pertencer ao intervalo ]0 ; 1]     |");
        System.out.println("|                                                          |");
        System.out.println("|----------------------------------------------------------|");
        System.out.println();
        System.out.println();
        input = in.nextDouble();
        while (input <= minimo || input > maximo) {
            System.out.println();
            System.out.println("|----------------------------------------------------------|");
            System.out.println("|                                                          |");
            System.out.println("|              O número introduzido é inválido!            |");
            System.out.println("|        O valor tem de pertencer ao intervalo ]0 ; 1]     |");
            System.out.println("|                                                          |");
            System.out.println("|----------------------------------------------------------|");
            System.out.println();
            System.out.println();
            input = in.nextDouble();
        }
        return input;
    }

    public static int lerDias(int minimo) {
        int num;
        System.out.println();
        System.out.println("|----------------------------------------------------------|");
        System.out.println("|                                                          |");
        System.out.println("|            Insere o numero de dias da amostra:           |");
        System.out.println("|             O valor tem de ser superior a zero           |");
        System.out.println("|                                                          |");
        System.out.println("|----------------------------------------------------------|");
        System.out.println();
        System.out.println();
        num = in.nextInt();

        while (num <= minimo) {
            System.out.println();
            System.out.println("|----------------------------------------------------------|");
            System.out.println("|                                                          |");
            System.out.println("|              O número introduzido é inválido!            |");
            System.out.println("|             O valor tem de ser superior a zero           |");
            System.out.println("|                                                          |");
            System.out.println("|----------------------------------------------------------|");
            System.out.println();
            System.out.println();
            num = in.nextInt();
        }

        return num;
    }

    public static int menu2() {

        int num, minimo = 0, maximo = 1;
        System.out.println();
        System.out.println("|----------------------------------------------------------|");
        System.out.println("|                                                          |");
        System.out.println("|           Escolha entre as seguintes opções:             |");
        System.out.println("|                                                          |");
        System.out.println("|                  1- Fazer um novo estudo.                |");
        System.out.println("|                    0- Fechar programa.                   |");
        System.out.println("|                                                          |");
        System.out.println("|----------------------------------------------------------|");
        System.out.println();
        System.out.println();

        num = in.nextInt();

        while (num < minimo || num > maximo) {
            System.out.println();
            System.out.println("|----------------------------------------------------------|");
            System.out.println("|                                                          |");
            System.out.println("|                     Opção inválida!                      |");
            System.out.println("|           Escolha entre as seguintes opções:             |");
            System.out.println("|                                                          |");
            System.out.println("|                  1- Fazer um novo estudo.                |");
            System.out.println("|                    0- Fechar programa.                   |");
            System.out.println("|                                                          |");
            System.out.println("|----------------------------------------------------------|");
            System.out.println();
            System.out.println();
            num = in.nextInt();
        }

        return num;

    }

    public static void preencherValoresIniciais(double[][] valores, double[] sir) {
        valores[0][0] = 0;
        valores[0][4] = sir[0] + sir[1] + sir[2];
        for (int i = 1; i <= 3; i++) {
            valores[0][i] = sir[i - 1];
        }
    }

    public static double[][] euler(double[][] parametros, double[] sir, int numDias, double h, int caso) throws IOException {
        int dia = 1;
        double[][] valores = new double[numDias + 1][5];
        preencherValoresIniciais(valores, sir);
        if ((1 - h) < 0) {
            h = 1;
        }
        int iteracoes_por_dia = (int) (1 / h);
        int numIteracoes = (iteracoes_por_dia * numDias);

        for (int i = 1; i <= numIteracoes; i++) {

            double[] variacao = calcularTaxasDeVariacao(sir, parametros, caso);

            sir[0] = sir[0] + h * variacao[0];
            sir[1] = sir[1] + h * variacao[1];
            sir[2] = sir[2] + h * variacao[2];

            if (i % iteracoes_por_dia == 0) {
                preencherValores(valores, sir, dia);
                dia++;
            }
        }
        return valores;
    }

    public static double[][] rk4(double[][] parametros, double[] sir, int numDias, double h, int caso) throws IOException {
        double[][] valores = new double[numDias + 1][5];
        preencherValoresIniciais(valores, sir);
        if ((1 - h) < 0) {
            h = 1;
        }
        int iteracoes_por_dia = (int) (1 / h);
        int numIteracoes = (iteracoes_por_dia * numDias);

        int dia = 1;

        for (int i = 1; i <= numIteracoes; i++) {
            double[] k1 = multipicarArrayPorConstante(calcularTaxasDeVariacao(sir, parametros, caso), h);
            double[] k2 = multipicarArrayPorConstante(calcularTaxasDeVariacao(somarArrays(sir, multipicarArrayPorConstante(k1, 0.5)), parametros, caso), h);
            double[] k3 = multipicarArrayPorConstante(calcularTaxasDeVariacao(somarArrays(sir, multipicarArrayPorConstante(k2, 0.5)), parametros, caso), h);
            double[] k4 = multipicarArrayPorConstante(calcularTaxasDeVariacao(somarArrays(sir, multipicarArrayPorConstante(k3, h)), parametros, caso), h);
            double[] k_final = multipicarArrayPorConstante(somarArrays(somarArrays(somarArrays(k1, multipicarArrayPorConstante(k2, 2)), multipicarArrayPorConstante(k3, 2)), k4), 0.16666666666666666666666666666666666666666666666667);

            sir = somarArrays(sir, k_final);


            if (i % iteracoes_por_dia == 0) {
                preencherValores(valores, sir, dia);
                dia++;
            }
        }
        return valores;
    }

    public static double[] calcularTaxasDeVariacao(double[] sir, double[][] parametros, int caso) {

        double dSdt = parametros[caso][1] - parametros[caso][5] * sir[0] * sir[1] - parametros[caso][2] * sir[0];
        double dIdt = parametros[caso][5] * sir[0] * sir[1] - parametros[caso][3] * sir[1] + parametros[caso][4] * sir[1] * sir[2] - (parametros[caso][2] + parametros[caso][6]) * sir[1];
        double dRdt = parametros[caso][3] * sir[1] - parametros[caso][4] * sir[1] * sir[2] - (parametros[caso][2] + parametros[caso][7]) * sir[2];

        return new double[]{dSdt, dIdt, dRdt};
    }

    public static void preencherValores(double[][] valores, double[] sir, int dia) {
        valores[dia][4] = round(sir[0] + sir[1] + sir[2]);
        valores[dia][3] = round(sir[2]);
        valores[dia][2] = round(sir[1]);
        valores[dia][1] = round(sir[0]);
        valores[dia][0] = dia;
    }

    public static double round(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(9, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double[] multipicarArrayPorConstante(double[] arr, double scalar) {
        double[] result = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i] * scalar;
        }
        return result;
    } //teste feito

    public static double[] somarArrays(double[] arr1, double[] arr2) {
        double[] result = new double[arr1.length];
        for (int i = 0; i < arr1.length; i++) {
            result[i] = arr1[i] + arr2[i];
        }
        return result;
    } //teste feito

    private static void escreverCSV(double[][] matriz, String caminhoArquivo) throws IOException {
        FileWriter writer = new FileWriter(caminhoArquivo);
        writer.write("Dia;S;I;R;T");
        writer.write("\n");
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if (j == 0) {
                    writer.write(valueOf(Math.round(matriz[i][j])));
                } else {
                    writer.write(valueOf(matriz[i][j]));
                }
                if (j < matriz[i].length - 1) {
                    writer.write(";");
                }
            }
            writer.write("\n");
        }
        writer.close();
    }


    public static void desenharGrafico(String scriptFileName) throws IOException {
        Process exec = rt.exec("C:\\gnuplot\\bin\\gnuplot " + scriptFileName);
    }

    public static String escreverScriptGNU(int numDias, int escolherMetodo, int caso) throws IOException {
        String scriptFileName = "ScriptsGNU/script_caso_" + (caso + 1) + ".gp";

        FileWriter writer = new FileWriter(scriptFileName);
        writer.write("set terminal png\n");
        if (escolherMetodo == 1) {
            writer.write("set output \"Graficos/grafico_Euler_");
        } else {
            writer.write("set output \"Graficos/grafico_RK4_");
            System.out.println();
        }
        writer.write("caso");
        writer.write(valueOf(caso + 1));
        writer.write(".png\"\n");

        writer.write("set datafile separator \";\"\n");
        writer.write("set xlabel \"Dia\"\n");
        writer.write("set ylabel \"Taxa\"\n");
        writer.write("set xrange [0:");
        writer.write(numDias + "]\n");
        writer.write("set yrange [0:1.15]\n");

        writer.write("plot ");
        if (escolherMetodo == 1) {
            writer.write("\"FicheirosCSV/matriz_resultado_Euler");
        } else {
            writer.write("\"FicheirosCSV/matriz_resultado_RK4");

        }
        writer.write("_caso");
        writer.write(valueOf(caso + 1));
        writer.write(".csv\" u 1:2 w l lw 2 lc 14 ps 2 t \"Suscetíveis\", \"\" u 1:3 w l lw 2 lc 7 ps 2 t \"Infetados\", \"\" u 1:4 w l lw 2 lc 10 ps 2 t \"Recuperados\"\n");
        writer.write("set grid\n");
        writer.close();
        return scriptFileName;
    }

    public static void escreverMensagemSucesso(int caso, int escolherMetodo){
        System.out.println();

        System.out.println("CASO " + (caso+1) + ":");
        caso = caso +1;
        String metodo;

        if(escolherMetodo == 1){
            metodo = "Euler";
        }
        else{
            metodo = "RK4";
        }

        System.out.println("Os resultados foram escritos com sucesso no ficheiro CSV: FicheirosCSV/matriz_resultado_" + metodo + "_caso" + caso + ".csv");
        System.out.println("O gráfico foi desenhado com sucesso: Graficos/grafico_" + metodo + "_caso" + (caso) + ".png");
    }
}

