package com.haogrgr.test.main;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Locale;

import javax.net.ssl.SSLServerSocketFactory;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpConnectionFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpServerConnection;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultBHttpServerConnection;
import org.apache.http.impl.DefaultBHttpServerConnectionFactory;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import org.apache.http.protocol.UriHttpRequestHandlerMapper;
import org.apache.http.util.EntityUtils;

/**
 * apache http core 官方例子
 * 
 * @author desheng.tu
 * @date 2015年5月18日 上午9:58:41 
 *
 */
public class HttpSrvMain {
	public static void main(String[] args) throws Exception {
		int port = 8080;
		String docRoot = "D:\\svn_file\\TEST2\\150503\\web-customer";

		// Set up the HTTP protocol processor
		HttpProcessor httpproc = HttpProcessorBuilder.create().add(new ResponseDate())
				.add(new ResponseServer("Haogrgr/1.1")).add(new ResponseContent())
				.add(new ResponseConnControl()).build();

		// Set up request handlers
		UriHttpRequestHandlerMapper reqistry = new UriHttpRequestHandlerMapper();
		reqistry.register("*", new HttpFileHandler(docRoot));

		// Set up the HTTP service
		HttpService httpService = new HttpService(httpproc, reqistry);

		Thread t = new RequestListenerThread(port, httpService, null);
		t.setDaemon(false);
		t.start();
	}

	static class HttpFileHandler implements HttpRequestHandler {

		private final String docRoot;

		public HttpFileHandler(final String docRoot) {
			super();
			this.docRoot = docRoot;
		}

		public void handle(final HttpRequest request, final HttpResponse response,
				final HttpContext context) throws HttpException, IOException {
			String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
			if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
				throw new MethodNotSupportedException(method + " method not supported");
			}
			String target = request.getRequestLine().getUri();

			if (request instanceof HttpEntityEnclosingRequest) {
				HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
				byte[] entityContent = EntityUtils.toByteArray(entity);
				System.out.println("Incoming entity content (bytes): " + entityContent.length);
			}

			final File file = new File(this.docRoot, URLDecoder.decode(target, "UTF-8"));
			if (!file.exists()) {
				response.setStatusCode(HttpStatus.SC_NOT_FOUND);
				StringEntity entity = new StringEntity("<html><body><h1>File" + file.getPath()
						+ " not found</h1></body></html>", ContentType.create("text/html", "UTF-8"));
				response.setEntity(entity);
				System.out.println("File " + file.getPath() + " not found");
			}
			else if (!file.canRead() || file.isDirectory()) {
				response.setStatusCode(HttpStatus.SC_FORBIDDEN);
				StringEntity entity = new StringEntity(
						"<html><body><h1>Access denied</h1></body></html>", ContentType.create(
								"text/html", "UTF-8"));
				response.setEntity(entity);
				System.out.println("Cannot read file " + file.getPath());
			}
			else {
				response.setStatusCode(HttpStatus.SC_OK);
				FileEntity body = new FileEntity(file, ContentType.create("text/html",
						(Charset) null));
				response.setEntity(body);
				System.out.println("Serving file " + file.getPath());
			}
		}
	}

	static class RequestListenerThread extends Thread {

		private final HttpConnectionFactory<DefaultBHttpServerConnection> connFactory;
		private final ServerSocket serversocket;
		private final HttpService httpService;

		public RequestListenerThread(final int port, final HttpService httpService,
				final SSLServerSocketFactory sf) throws IOException {
			this.connFactory = DefaultBHttpServerConnectionFactory.INSTANCE;
			this.serversocket = sf != null ? sf.createServerSocket(port) : new ServerSocket(port);
			this.httpService = httpService;
		}

		@Override
		public void run() {
			System.out.println("Listening on port " + this.serversocket.getLocalPort());
			while (!Thread.interrupted()) {
				try {
					// Set up HTTP connection
					Socket socket = this.serversocket.accept();
					System.out.println("Incoming connection from " + socket.getInetAddress());
					HttpServerConnection conn = this.connFactory.createConnection(socket);

					// Start worker thread
					Thread t = new WorkerThread(this.httpService, conn);
					t.setDaemon(true);
					t.start();
				}
				catch (InterruptedIOException ex) {
					break;
				}
				catch (IOException e) {
					System.err.println("I/O error initialising connection thread: "
							+ e.getMessage());
					break;
				}
			}
		}
	}

	static class WorkerThread extends Thread {

		private final HttpService httpservice;
		private final HttpServerConnection conn;

		public WorkerThread(final HttpService httpservice, final HttpServerConnection conn) {
			super();
			this.httpservice = httpservice;
			this.conn = conn;
		}

		@Override
		public void run() {
			System.out.println("New connection thread");
			HttpContext context = new BasicHttpContext(null);
			try {
				while (!Thread.interrupted() && this.conn.isOpen()) {
					this.httpservice.handleRequest(this.conn, context);
				}
			}
			catch (ConnectionClosedException ex) {
				System.err.println("Client closed connection");
			}
			catch (IOException ex) {
				System.err.println("I/O error: " + ex.getMessage());
			}
			catch (HttpException ex) {
				System.err.println("Unrecoverable HTTP protocol violation: " + ex.getMessage());
			}
			finally {
				try {
					this.conn.shutdown();
				}
				catch (IOException ignore) {
				}
			}
		}

	}
}
