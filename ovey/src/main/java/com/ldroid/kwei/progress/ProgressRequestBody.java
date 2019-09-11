/**
 * Copyright (c) 2015-present, Facebook, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.ldroid.kwei.progress;


import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;

public class ProgressRequestBody extends RequestBody {

  private final RequestBody mRequestBody;
  private final ProgressListener mProgressListener;
  private BufferedSink mBufferedSink;
  private long mContentLength = 0L;

  public ProgressRequestBody(RequestBody requestBody, ProgressListener progressListener) {
    mRequestBody = requestBody;
    mProgressListener = progressListener;
  }

  @Override
  public MediaType contentType() {
    return mRequestBody.contentType();
  }

  @Override
  public long contentLength() throws IOException {
    if (mContentLength == 0) {
      mContentLength = mRequestBody.contentLength();
    }
    return mContentLength;
  }

  @Override
  public void writeTo(BufferedSink sink) throws IOException {
    if (mBufferedSink == null) {
      mBufferedSink = Okio.buffer(outputStreamSink(sink));
    }

    // contentLength changes for input streams, since we're using inputStream.available(),
    // so get the length before writing to the sink
    contentLength();

    mRequestBody.writeTo(mBufferedSink);
    mBufferedSink.flush();
  }

  private Sink outputStreamSink(BufferedSink sink) {
    return Okio.sink(new CountingOutputStream(sink.outputStream()) {
      @Override
      public void write(byte[] data, int offset, int byteCount) throws IOException {
        super.write(data, offset, byteCount);
        sendProgressUpdate();
      }

      @Override
      public void write(int data) throws IOException {
        super.write(data);
        sendProgressUpdate();
      }

      private void sendProgressUpdate() throws IOException {
        long bytesWritten = getCount();
        long contentLength = contentLength();
        mProgressListener.onProgress(
          bytesWritten, contentLength, bytesWritten == contentLength);
      }
    });
  }
}
