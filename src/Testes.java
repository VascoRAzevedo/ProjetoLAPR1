import java.io.File;
import java.io.FileNotFoundException;

public class Testes {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Teste ler valores iniciais: " + testLerValIniciais());
        System.out.println("Teste calcular taxas de variação: " + testCalcularTaxas());
        System.out.println("Teste preencher valores inciais: " + testPreencherValoresIniciais());
        System.out.println("Teste preencher valores: " + testPreencherValores());
        System.out.println("Teste multiplicar array: " + testMultiplicarArray());
        System.out.println("Teste somar array: " + testSomarArrays());

    }

    public static boolean testLerValIniciais() throws FileNotFoundException {
        File teste = new File("val_iniciais_teste.csv");
        double[] resultadoEsperado = {0.7,0.2,0.1};
        double[] resultadoObtido = Main.lerValIniciais(teste);

        for (int i = 0; i < resultadoEsperado.length; i++) {
            if (resultadoEsperado[i] != resultadoObtido[i]) {
                return false;
            }
        }

        return true;
    }



    public static boolean testMultiplicarArray() {
        double[] array = {1, 0, 3, 4};
        double escalar = 3;
        double[] resultadoEsperado = {3, 0, 9, 12};
        double[] resultadoObtido = Main.multipicarArrayPorConstante(array, escalar);

        for (int i = 0; i < resultadoEsperado.length; i++) {
            if (resultadoEsperado[i] != resultadoObtido[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean testSomarArrays() {
        double[] array1 = {1, 0, 3, 0};
        double[] array2 = {3, -2, 22, 0};
        double[] resultadoEsperado = {4, -2, 25, 0};
        double[] resultadoObtido = Main.somarArrays(array1, array2);

        for (int i = 0; i < resultadoEsperado.length; i++) {
            if (resultadoEsperado[i] != resultadoObtido[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean testPreencherValores() {

        double[][] resultadoEsperado = {
                {0, 1, 2, 3, 6},
                {1, 1, 2, 3, 6},
                {2, 1, 2, 3, 6},
        };

        double[] arrayInserir = {1, 2, 3,};

        double[][] resultadoObtido = new double[3][5];

        for (int i = 0; i < 3; i++) {
            Main.preencherValores(resultadoObtido, arrayInserir, i);
        }

        for (int i = 0; i < resultadoEsperado.length; i++) {

            for (int j = 0; j < resultadoEsperado[0].length; j++) {
                if (resultadoEsperado[i][j] != resultadoObtido[i][j]) {
                    return false;
                }

            }


        }


        return true;
    }

    public static boolean testPreencherValoresIniciais() {

        double[][] resultadoEsperado = {
                {0, 1, 2, 3, 6},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
        };

        double[] arrayInserir = {1, 2, 3,};

        double[][] resultadoObtido = new double[3][5];

        for (int i = 0; i < 3; i++) {
            Main.preencherValoresIniciais(resultadoObtido, arrayInserir);
        }

        for (int i = 0; i < resultadoEsperado.length; i++) {

            for (int j = 0; j < resultadoEsperado[0].length; j++) {
                if (resultadoEsperado[i][j] != resultadoObtido[i][j]) {
                    return false;
                }

            }


        }
        return true;
    }

    public static boolean testCalcularTaxas(){
        int caso = 1;
        double[] sir = {0.5 , 0.3 , 0.2};

        double[][] parametros = {
                {1, 0.04, 0.04, 0.02, 0.03, 3, 0, 0},
                {1, 0.04, 0.04, 0.02, 0.03, 0.02, 0, 0},
                {1, 0.03, 0.0222, 0.22, 1.03, 0.02, 3, 0},
        };

        double[] resultadoEsperado = {0.016999999999999998 , -0.0132 , -0.0037999999999999996};


        double[] resultadoObtido = Main.calcularTaxasDeVariacao(sir, parametros, caso);


        sir[0] = sir[0] + 0.5 * resultadoObtido[0];
        sir[1] = sir[1] + 0.5 * resultadoObtido[1];
        sir[2] = sir[2] + 0.5 * resultadoObtido[2];



        for (int i = 0; i < resultadoObtido.length; i++) {
            if (resultadoEsperado[i] != resultadoObtido[i]){
                return false;
            }
        }

        return true;
    }

}