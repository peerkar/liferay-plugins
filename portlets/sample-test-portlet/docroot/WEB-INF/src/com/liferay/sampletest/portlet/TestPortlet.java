/**
 * Copyright (c) 2000-2009 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.sampletest.portlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortlet;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.portlet.PortletRequestUtil;
import com.liferay.util.servlet.PortletResponseUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

/**
 * <a href="TestPortlet.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class TestPortlet extends LiferayPortlet {

	public void doDispatch(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		String jspPage = ParamUtil.getString(
			renderRequest, "jspPage", "/view.jsp");

		if (jspPage.equals("/renderResponseponse/buffer_size.jsp")) {
			testResponseBufferSize(renderResponse);
		}

		include(jspPage, renderRequest, renderResponse);
	}

	public void serveResource(
			ResourceRequest renderRequest, ResourceResponse renderResponse)
		throws IOException, PortletException {

		String fileName = renderRequest.getResourceID();
		InputStream is = getPortletContext().getResourceAsStream(
			"/WEB-INF/images/logo.png");
		String contentType = MimeTypesUtil.getContentType(fileName);

		PortletResponseUtil.sendFile(renderResponse, fileName, is, contentType);
	}

	public void uploadForm1(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		UploadPortletRequest uploadRequest = PortalUtil.getUploadPortletRequest(
			actionRequest);

		String actionRequestTitle = ParamUtil.getString(actionRequest, "title");
		String uploadRequestTitle = ParamUtil.getString(uploadRequest, "title");

		File file = uploadRequest.getFile("fileName");

		if (_log.isInfoEnabled()) {
			_log.info("actionRequestTitle " + actionRequestTitle);
			_log.info("uploadRequestTitle " + uploadRequestTitle);
			_log.info("File " + file + " " + file.length());
		}
	}

	public void uploadForm2(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		PortletRequestUtil.testMultipartWithCommonsFileUpload(actionRequest);
	}

	public void uploadForm3(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		PortletRequestUtil.testMultipartWithPortletInputStream(actionRequest);
	}

	protected void include(
			String path, RenderRequest renderRequest,
			RenderResponse renderResponse)
		throws IOException, PortletException {

		PortletRequestDispatcher portletRequestDispatcher =
			getPortletContext().getRequestDispatcher(path);

		if (portletRequestDispatcher == null) {
			_log.error(path + " is not a valid include");
		}
		else {
			portletRequestDispatcher.include(renderRequest, renderResponse);
		}
	}

	protected void testResponseBufferSize(RenderResponse renderResponse) {
		_log.info("Original buffer size " + renderResponse.getBufferSize());

		renderResponse.setBufferSize(12345);

		_log.info("New buffer size " + renderResponse.getBufferSize());
	}

	private static Log _log = LogFactoryUtil.getLog(TestPortlet.class);

}