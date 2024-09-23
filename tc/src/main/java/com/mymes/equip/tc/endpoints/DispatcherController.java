package com.mymes.equip.tc.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mymes.equip.tc.ToolControlException;
import com.mymes.equip.tc.dispatcher.MessageDispatcher;
import com.mymes.equip.tc.dispatcher.TcMessageRequest;
import com.mymes.equip.tc.dispatcher.TcMessageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name="Dispatcher", description="API for Clients which send message via HTTP/REST to read / write in PLC")
@RestController
@RequestMapping(value="/api/v1/ifsvc")
@Slf4j
public class DispatcherController {

	@Autowired
	private MessageDispatcher messageDispatcher;

	@Operation(
		summary="Request for PLC read or write with async manner",
		description="Request for PLC read or write with async manner"
	)
	@ApiResponse(
		responseCode="200",
		description="Successful to read or write but no reply data"
	)
	@PostMapping(value="/request")
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	public void service (@RequestBody TcMessageRequest req) throws ToolControlException {
		log.debug("DispatcherController.service (TcMessageRequest req)...");

		req.setShouldReply(false);
		messageDispatcher.dispatch(req);
	}

	@Operation(
		summary="Request for PLC read or write with sync manner",
		description="Request for PLC read or write with sync manner"
	)
	@ApiResponse(
		responseCode="200",
		description="TcMessageResponse"
	)
	@PostMapping(value="/request-and-reply")
	@ResponseStatus(value = HttpStatus.OK)
	@CrossOrigin
	@ResponseBody
	public TcMessageResponse serviceWithSyncReply (@RequestBody TcMessageRequest req) throws ToolControlException {
		log.debug("DispatcherController.serviceWithSyncReply (TcMessageRequest req)...");

		req.setShouldReply(true);
		req.setPushResult(false);
		TcMessageResponse response=new TcMessageResponse();
		messageDispatcher.dispatch(req, response);

		return response;
	}
}
