package com.haogrgr.test.main;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Executors;

import com.google.common.collect.ImmutableSet;

public class FileAioTest {

	public static void main(String[] args) throws Exception {
		Path path = Paths.get("F:\\iso\\ubuntu-14.04.2-desktop-amd64.iso");

		ByteBuffer buffer = ByteBuffer.allocate(8192);

		AsynchronousFileChannel channel = AsynchronousFileChannel.open(path, ImmutableSet.of(StandardOpenOption.READ),
				Executors.newSingleThreadExecutor());

		channel.read(buffer, 0, buffer, new FrameCompletionHandler());

		Thread.sleep(100000);
	}

	public static class FrameCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

		@Override
		public void completed(Integer result, ByteBuffer attachment) {
			System.out.println(attachment);
		}

		@Override
		public void failed(Throwable exc, ByteBuffer attachment) {
			exc.printStackTrace();
		}

	}
}