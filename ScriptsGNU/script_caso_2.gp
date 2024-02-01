set terminal png
set output "Graficos/grafico_Euler_caso2.png"
set datafile separator ";"
set xlabel "Dia"
set ylabel "Taxa"
set xrange [0:200]
set yrange [0:1.15]
plot "FicheirosCSV/matriz_resultado_Euler_caso2.csv" u 1:2 w l lw 2 lc 14 ps 2 t "Suscetíveis", "" u 1:3 w l lw 2 lc 7 ps 2 t "Infetados", "" u 1:4 w l lw 2 lc 10 ps 2 t "Recuperados"
set grid
