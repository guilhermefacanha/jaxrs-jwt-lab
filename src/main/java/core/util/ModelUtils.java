package core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import core.entity.EntityBase;

public class ModelUtils {
	private static String[] meses = { "JAN", "FEV", "MAR", "ABR", "MAI", "JUN", "JUL", "AGO", "SET", "OUT", "NOV",
			"DEZ" };

	public static int getMesInteger(String mes) {
		for (int i = 0; i < meses.length; i++) {
			if (meses[i].equals(mes))
				return i + 1;
		}

		return -1;
	}

	public static boolean isNullEmpty(String texto) {
		return texto == null || texto.equals("");
	}

	public static String getFileContentType(String name) {
		try {
			FileNameMap fileNameMap = URLConnection.getFileNameMap();
			return fileNameMap.getContentTypeFor(name);
		} catch (Exception e) {
			return "";
		}
	}

	public static String getMesExtenso(int mes) {
		return meses[mes - 1];
	}

	public static String getMesExtenso(String mesAno) {
		int mes = Integer.parseInt(mesAno.split("/")[0]);
		return meses[mes - 1] + "/" + mesAno.split("/")[1];
	}

	public static String getPrimeiroDia(String mesAno) {
		return "01/" + mesAno;
	}

	public static String getPrimeiroDiaProximoMes(String mesAno) {
		String mesStr = mesAno.split("/")[0];
		String anoStr = mesAno.split("/")[1];
		Integer mes = Integer.parseInt(mesStr);
		mes = mes + 1;
		if (mes < 10)
			mesStr = "0" + mes;

		return "01/" + mesStr + "/" + anoStr;
	}

	public static String getExtensaoArquivo(String name) {
		try {
			int lastIndex = name.lastIndexOf(".");
			return name.substring(lastIndex, name.length());
		} catch (Exception e) {
			return "";
		}
	}

	public static <T extends EntityBase> boolean isObjetoValido(T obj) {
		if (obj != null && obj.getId() > 0)
			return true;
		else
			return false;
	}

	public static <T extends EntityBase> EntityBase toNullObject(T obj) {
		if (obj != null && obj.getId() == 0)
			obj = null;
		return obj;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isListaValida(List lista) {
		return lista != null && lista.size() > 0;
	}

	public static java.sql.Date getStringParaData(String d1) throws Exception {

		DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
		java.sql.Date data = null;
		try {
			data = new java.sql.Date(formatter.parse(d1).getTime());
		} catch (ParseException e) {
			throw new Exception(e.getMessage());
		}

		return data;
	}

	public static String formatarNumero(long numero, int posicoes) {
		String res = String.valueOf(numero);
		while (res.length() < posicoes)
			res = "0" + res;

		return res;
	}

	public static String formatarValor(double valor) {
		NumberFormat f = NumberFormat.getInstance();
		f.setMaximumFractionDigits(2);
		f.setMinimumFractionDigits(2);
		return f.format(valor);
	}

	public static String getValorPrint(double valor) {
		NumberFormat moneyFormat = DecimalFormat.getCurrencyInstance(new Locale("pt", "BR"));
		moneyFormat.setMinimumFractionDigits(2);
		moneyFormat.setMaximumFractionDigits(2);
		return moneyFormat.format(valor);
	}

	public static double getValorFormatado(String valor) throws ParseException {
		NumberFormat f = NumberFormat.getInstance();
		f.setMaximumFractionDigits(2);
		f.setMinimumFractionDigits(2);
		return f.parse(valor).doubleValue();
	}

	public static String gerarHash(String texto) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(texto.getBytes());
		byte[] hash = md.digest();

		StringBuilder s = new StringBuilder();
		for (int i = 0; i < hash.length; i++) {
			int parteAlta = ((hash[i] >> 4) & 0xf) << 4;
			int parteBaixa = hash[i] & 0xf;
			if (parteAlta == 0)
				s.append('0');
			s.append(Integer.toHexString(parteAlta | parteBaixa));
		}
		return s.toString();

	}

	public static String gerarBase64(String texto) {
		return Base64.getEncoder().encodeToString(texto.getBytes());
	}

	public static String lerBase64(String texto) {
		return new String(Base64.getDecoder().decode(texto));
	}

	public static String pularLinha() {
		return System.getProperty("line.separator");
	}

	public static Date ParseStringDate(String texto) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date data = null;
		try {
			data = format.parse(texto);
		} catch (Exception e) {
		}
		return data;
	}

	public static Date ParseStringDate(String texto, String formato) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(formato);
		Date data = null;
		try {
			data = format.parse(texto);
		} catch (Exception e) {
		}
		return data;
	}

	public static String getStringDeData(Date data) {
		if (data != null) {
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			return formatter.format(data).toString();
		}
		return "";
	}

	public static String getStringDeData(Date data, String formato) {
		DateFormat formatter = new SimpleDateFormat(formato);
		return formatter.format(data).toString();
	}

	public static String formataDataPadrao(String data) {
		String[] auxiliar = data.split("-");

		data = auxiliar[2] + "/" + auxiliar[1] + "/" + auxiliar[0];

		return data;
	}

	public static String formataDataPadrao(java.sql.Date dateSql) {
		String data = dateSql.toString();

		String[] auxiliar = data.split("-");

		data = auxiliar[2] + "/" + auxiliar[1] + "/" + auxiliar[0];

		return data;
	}

	public static String getResumoTexto(String texto) {
		String resul = "";

		if (texto.length() >= 50) {
			resul = texto.substring(0, 49) + "...";
			return resul;
		} else {
			return texto;
		}

	}

	public static int tryParse(String s) {
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			return 0;
		}
	}

	public static String FormataTexto(String texto) {
		String resul = "";

		resul = texto.replace("\n", "<br/>");

		return resul;
	}

	public static void copiarArquivo(File fonte, File destino) throws IOException {
		verificarDiretorio(destino.getParentFile());

		InputStream in = new FileInputStream(fonte);
		OutputStream out = new FileOutputStream(destino);

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	public static void copiarArquivo(String fonte, String destino) throws IOException {
		File fFonte = new File(fonte);
		File fDestino = new File(destino);

		verificarDiretorio(fDestino.getParentFile());

		InputStream in = new FileInputStream(fFonte);
		OutputStream out = new FileOutputStream(fDestino);

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	public static void gravarArquivo(byte[] conteudo, String caminho) throws IOException {
		OutputStream out = new FileOutputStream(caminho);
		out.write(conteudo);
		out.close();
	}

	public static void verificarDiretorio(File dir) {
		if (!dir.isDirectory())
			dir.mkdirs();
	}

	public static void verificarDiretorio(String str) {
		File dir = new File(str);

		if (!dir.isDirectory())
			dir.mkdirs();
	}

	/**
	 * Remover acentuacao de Strings
	 */
	public static String removerAcentuacao(String texto) {
		texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
		texto = texto.replaceAll("[^\\p{ASCII}]", "");
		return texto;
	}

	public static String retirarCaracteresEspeciais(String texto) {
		texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
		texto = texto.replaceAll("[^a-zZ-Z0-9]", "");
		return texto;
	}

	public static String normalizarNomeArquivo(String nome) {
		nome = removerAcentuacao(nome);
		nome = nome.replace(" ", "_");
		return nome;
	}

}
