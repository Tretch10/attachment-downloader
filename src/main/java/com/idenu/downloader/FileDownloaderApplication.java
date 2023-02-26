package com.idenu.downloader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class FileDownloaderApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(FileDownloaderApplication.class, args);

		String url = "https://www.google.com/";
		String folderPath = "/Documents/GoogleAttachments";
		List<String> attachmentUrls = getAttachmentUrls(url);

		for (String attachmentUrl : attachmentUrls){
			downloadAttachment(attachmentUrl, folderPath);
		}
	}
	//Method to get all attachments on website
	private static List<String> getAttachmentUrls(String url) throws IOException {
		List<String> attachmentUrls = new ArrayList<>();
		Document document = Jsoup.connect(url).get();
		Elements links = document.select("a[href]");

		for(Element link: links){
			String href = link.attr("href");
			if (href.endsWith(".pdf") || href.endsWith(".doc") || href.endsWith(".docx") || href.endsWith(".png")) {
				attachmentUrls.add(href);
			}
		}
		return attachmentUrls;
	}

	private static void downloadAttachment(String attachmentUrl, String folderPath){
		try (BufferedInputStream in = new BufferedInputStream(new URL(attachmentUrl).openStream());
			 FileOutputStream fileOutputStream = new FileOutputStream(new File(folderPath + File.separator + attachmentUrl.substring(attachmentUrl.lastIndexOf('/') + 1)))) {
			byte dataBuffer[] = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
				fileOutputStream.write(dataBuffer, 0, bytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
