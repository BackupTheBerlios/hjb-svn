/*
 HJB (HTTP JMS Bridge) links the HTTP protocol to the JMS API.
 Copyright (C) 2006 Timothy Emiola

 HJB is free software; you can redistribute it and/or modify it under
 the terms of the GNU Lesser General Public License as published by the
 Free Software Foundation; either version 2.1 of the License, or (at
 your option) any later version.

 This library is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301
 USA

 */
package hjb.misc;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * <code>HJBStrings</code> provides access to the message resources used in
 * the <code>hjb</code> subpackages.
 * 
 * @author Tim Emiola
 */
public final class HJBStrings {

    public HJBStrings() {
        loadResources();
    }

    public String getString(String key) {
        try {
            if (null == key) return "";
            return getResources().getString(key);
        } catch (MissingResourceException e) {
            return handleMissingResource(key);
        }
    }

    public String getString(String key, Object[] arguments) {
        String formatString = null;
        try {
            formatString = getResources().getString(key);
            if (null == key) return "";
            Object[] replaceAnyNulls = null == arguments ? new Object[0]
                    : new Object[arguments.length];
            for (int i = 0; i < replaceAnyNulls.length; i++) {
                replaceAnyNulls[i] = null == arguments[i] ? "" + null
                        : arguments[i];
            }
            return new MessageFormat(formatString).format(replaceAnyNulls);
        } catch (MissingResourceException e) {
            return handleMissingResource(key);
        } catch (IllegalArgumentException e) {
            return handleBadMessageFormat(key, formatString);
        }
    }

    public String getString(String key, Object arg1) {
        return getString(key, new Object[] {
            arg1
        });
    }

    public String getString(String key, Object arg1, Object arg2) {
        return getString(key, new Object[] {
                arg1, arg2
        });
    }

    public String getString(String key, Object arg1, Object arg2, Object arg3) {
        return getString(key, new Object[] {
                arg1, arg2, arg3
        });
    }

    public String getString(String key,
                            Object arg1,
                            Object arg2,
                            Object arg3,
                            Object arg4) {
        return getString(key, new Object[] {
                arg1, arg2, arg3, arg4
        });
    }

    public String needsANonNull(String name) {
        return getString(NEEDS_A_NON_NULL, name);
    }

    public String needsANonNull(Class clazz) {
        return needsANonNull(null == clazz ? null : clazz.getName());
    }

    public boolean equals(Object o) {
        return (o instanceof HJBStrings);
    }

    public int hashCode() {
        return this.getClass().getName().hashCode();
    }

    protected ResourceBundle getResources() {
        return RESOURCES;
    }

    protected void loadResources() {
        synchronized (HJBStrings.class) {
            RESOURCES = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME);
        }
    }

    protected String handleMissingResource(String key) {
        return MISSING_RESOURCE.format(new Object[] {
            "" + key
        });
    }

    protected String handleBadMessageFormat(String key, String format) {
        return "Tried to write message corresponding to ["
                + key
                + "] but the format used was bad, please contact the developers. [formatString='"
                + format + "']";
    }

    private static ResourceBundle RESOURCES;

    /**
     * Constant that holds the name of the resource bundle used from which the
     * resources are retrieved.
     * <p />
     * The value of this constant is "hjb.misc.HJBStrings".
     */
    public static final String RESOURCE_BUNDLE_NAME = "hjb.misc.HJBStrings";

    public static final MessageFormat MISSING_RESOURCE = new MessageFormat("Tried to write message corresponding to [{0}],"
            + " but no value for this string is present in the localization"
            + " resources, please contact the developers");

    public static final String NOT_APPLICAPLE = "not.applicaple";

    public static final String UNKNOWN_CAUSE = "unknown.cause";

    public static final String INVALID_DESTINATION_URL = "invalid.destination.url";

    public static final String CAN_NOT_USE_DESTINATION = "can.not.use.destination";

    public static final String INVALID_SUBSCRIBER_NAME = "invalid.subscriber.name";

    public static final String INVALID_MESSAGE = "invalid.message";

    public static final String INVALID_DESTINATION_TYPE = "invalid.destination.type";

    public static final String IGNORE_AND_DEFAULT_WARNING = "ignore.and.default.warning";

    public static final String BAD_VALUES_IN_PARAMETER_MAP = "bad.values.in.parameter.map";

    public static final String COMMAND_EXECUTION_WAS_INTERRUPTED = "command.execution.was.interrupted";

    public static final String COULD_NOT_INITIALISE_HJB_SERVLET = "could.not.initialise.hjb.servlet";

    public static final String HJB_ROOT_IS_AWOL = "hjb.root.is.awol";

    public static final String HELLO_FROM_HJB = "hello.from.hjb";

    public static final String REQUEST_PATH_IS_NOT_VALID = "request.path.is.not.valid";

    public static final String DESCRIPTION_OF_SEND_HJB_MESSAGE = "description.of.send.hjb.message";

    public static final String DESCRIPTION_OF_VIEW_QUEUE = "description.of.view.queue";

    public static final String DESCRIPTION_OF_RECEIVE_FROM_CONSUMER = "description.of.receive.from.consumer";

    public static final String DESCRIPTION_OF_RECEIVE_FROM_SUBSCRIBER = "description.of.receive.from.subscriber";

    public static final String DESCRIPTION_OF_SESSION_COMMIT = "description.of.session.commit";

    public static final String DESCRIPTION_OF_SESSION_ROLLBACK = "description.of.session.rollback";

    public static final String DESCRIPTION_OF_LISTING = "description.of.listing";

    public static final String DESCRIPTION_OF_READ_META_DATA = "description.of.read.meta.data";

    public static final String SUCCESS_MESSAGE_OF_READ_META_DATA = "success.message.of.read.meta.data";

    public static final String DESCRIPTION_OF_START_CONNECTION = "description.of.start.connection";

    public static final String SUCCESS_MESSAGE_OF_START_CONNECTION = "success.message.of.start.connection";

    public static final String DESCRIPTION_OF_STOP_CONNECTION = "description.of.stop.connection";

    public static final String SUCCESS_MESSAGE_OF_STOP_CONNECTION = "success.message.of.stop.connection";

    public static final String DESCRIPTION_OF_REGISTRATION_COMMANDS = "description.of.registration.commands";

    public static final String DESCRIPTION_OF_DELETE_COMMANDS = "description.of.delete.commands";

    public static final String DESCRIPTION_OF_CREATE_COMMANDS = "description.of.create.commands";

    public static final String NEEDS_A_NON_NULL = "needs.a.non.null";

    public static final String ALLOWED_PATH_NOT_FOUND = "allowed.path.not.found";

    public static final String SUCCESS_MESSAGE_OF_CREATE_COMMANDS = "success.message.of.create.commands";

    public static final String SUCCESS_MESSAGE_OF_REGISTRATION_COMMANDS = "success.message.of.registration.commands";

    public static final String SUCCESS_MESSAGE_OF_DELETE_COMMANDS = "success.message.of.delete.commands";

    public static final String SUCCESS_MESSAGE_OF_SESSION_COMMIT = "success.message.of.session.commit";

    public static final String SUCCESS_MESSAGE_OF_SESSION_ROLLBACK = "success.message.of.session.rollback";

    public static final String SUCCESS_MESSAGE_OF_LISTING = "success.message.of.listing";

    public static final String MESSAGE_SENT_OK = "message.sent.ok";

    public static final String MESSAGE_RECEIVED_OK = "message.received.ok";

    public static final String STRINGS_RECEIVED_OK = "messages.received.ok";

    public static final String MESSAGE_COPIERS_NOT_FOUND = "message.copiers.not.found";

    public static final String LOADED_MESSAGE_COPIERS = "loaded.message.copiers";

    public static final String NO_DEPLOYED_MESSAGE_COPIERS = "no.deployed.message.copiers";

    public static final String COULD_NOT_BUILD_PROVIDER = "could.not.build.provider";

    public static final String COULD_NOT_GET_MAP_FROM_TEXT = "could.not.get.map.from.text";

    public static final String JMS_COMMAND_NOT_GENERATED = "jms.command.not.generated";

    public static final String JMS_COMMAND_RUNNER_NOT_ASSIGNED = "jms.command.runner.not.assigned";

    public static final String HJB_RUNNER_DIED = "hjb.runner.died";

    public static final String HJB_RUNNER_STARTED = "hjb.runner.started";

    public static final String HJB_RUNNER_FINISHED = "hjb.runner.finished";

    public static final String HJB_RUNNER_REFUSED_COMMAND = "hjb.runner.refused.command";

    public static final String HJB_RUNNER_IGNORED_NULL_COMMAND = "hjb.runner.ignored.null.command";

    public static final String HJB_RUNNER_WAS_TERMINATED = "hjb.runner.was.terminated";

    public static final String IGNORED_NULL_HJB_MESSAGE = "ignored.null.hjb.message";

    public static final String DECODER_MISMATCH = "decoder.mismatch";

    public static final String STRING_VALUE_WAS_NULL = "string.value.was.null";

    public static final String INCORRECT_FORMAT_IN_MAP_DATA = "incorrect.format.in.map.data";

    public static final String WRONG_TYPE_TO_ENCODE = "wrong.type.to.encode";

    public static final String INCORRECT_JMS_MESSAGE_TYPE = "incorrect.jms.message.type";

    public static final String COULD_NOT_WRITE_DECODED_BYTES = "could.not.write.decoded.bytes";

    public static final String COULD_NOT_READ_ALL_BYTES = "could.not.read.all.bytes";

    public static final String COULD_NOT_COPY_OBJECT_MESSAGE = "could.not.copy.object.message";

    public static final String OBJECT_CLASS_MISSING = "object.class.missing";

    public static final String IGNORED_MESSAGE_LISTENER = "ignored.message.listener";

    public static final String COULD_NOT_CREATE_MESSAGE_COPIER = "could.not.create.message.copier";

    public static final String COULD_NOT_FIND_MESSAGE_COPIER = "could.not.find.message.copier";

    public static final String COULD_NOT_COPY_MESSAGE = "could.not.copy.message";

    public static final String COULD_NOT_WRITE_VALUE_TO_MESSAGE = "could.not.write.value.to.message";

    public static final String COULD_NOT_READ_VALUE_FROM_MESSAGE = "could.not.read.value.from.message";

    public static final String COULD_NOT_ACCESS_JMS_ATTRIBUTE_FROM_MESSAGE = "could.not.access.jms.attribute.from.message";

    public static final String COULD_NOT_ACCESS_CONNECTION_METADATA_ATTRIBUTE = "could.not.access.connection.metadata.attribute";

    public static final String WRONG_TYPE_READ_FROM_A_STREAM_MESSAGE = "wrong.type.read.from.a.stream.message";

    public static final String IGNORED_HJB_HTTP_HEADER = "ignored.hjb.http.header";

    public static final String COULD_NOT_GET_MESSAGE_PROPERTIES = "could.not.get.message.properties";

    public static final String OPTIONAL_JMS_NOT_SUPPORTED = "optional.jms.not.supported";

    public static final String COULD_NOT_FIND_CODEC_TO_DECODE = "could.not.find.codec.to.decode";

    public static final String COULD_NOT_FIND_CODEC_TO_ENCODE = "could.not.find.codec.to.encode";

    public static final String RESOURCE_NOT_FOUND = "resource.not.found";

    public static final String NOT_REMOVED = "not.removed";

    public static final String PROVIDER_NOT_REMOVED = "provider.not.removed";

    public static final String NO_PROVIDER_NAME_PRESENT = "no.provider.name.present";

    public static final String REGISTRATION_IGNORED = "registration.ignored";

    public static final String COULD_NOT_REGISTER = "could.not.register";

    public static final String CONNECTION_NOT_FOUND = "connection.not.found";

    public static final String SESSION_NOT_FOUND = "session.not.found";

    public static final String COMMAND_RUNNER_NOT_FOUND = "command.runner.not.found";

    public static final String PRODUCER_NOT_FOUND = "producer.not.found";

    public static final String SUBSCRIBER_NOT_FOUND = "subscriber.not.found";

    public static final String CONSUMER_NOT_FOUND = "consumer.not.found";

    public static final String BROWSER_NOT_FOUND = "browser.not.found";

    public static final String COULD_NOT_STOP_CONNECTION = "could.not.stop.connection";

    public static final String COULD_NOT_START_CONNECTION = "could.not.start.connection";

    public static final String COULD_NOT_CREATE_SUBSCRIBER = "could.not.create.subscriber";

    public static final String COULD_NOT_CREATE_PRODUCER = "could.not.create.producer";

    public static final String COULD_NOT_CREATE_CONSUMER = "could.not.create.consumer";

    public static final String COULD_NOT_CREATE_BROWSER = "could.not.create.browser";

    public static final String COULD_NOT_OBTAIN_CONNECTION_METADATA = "could.not.obtain.connection.metadata";

    public static final String PROVIDER_NAME_ALREADY_USED = "provider.name.already.used";

    public static final String COULD_NOT_CREATE_JMS_MESSAGE = "could.not.create.jms.message";

    public static final String COULD_NOT_OBTAIN_EXCEPTION_LISTENER = "could.not.obtain.exception.listener";

    public static final String PROVIDER_WITH_PARAMETERS_EXISTS = "provider.with.parameters.exists";

    public static final String COULD_NOT_CLOSE_CONNECTION = "could.not.close.connection";

    public static final String COULD_NOT_CLOSE_SESSION = "could.not.close.session";

    public static final String COULD_NOT_ASSIGN_EXCEPTION_LISTENER = "could.not.assign.exception.listener";

    public static final String NO_JMS_MESSAGE_CLASS_SPECIFIED = "no.jms.message.class.specified";

    public static final String NO_MESSAGE_AVAILABLE = "no.message.available";

    public static final String MANY_JMS_MESSAGE_INTERFACES = "many.jms.message.interfaces";

    public static final String COULD_NOT_BROWSE_QUEUE = "could.not.browse.queue";

    public static final String IS_NOT_A_JMS_MESSAGE = "is.not.a.jms.message";

    public static final String SEND_OF_MESSAGE_FAILED = "send.of.message.failed";

    public static final String RECEIVE_OF_MESSAGE_FAILED = "receive.of.message.failed";

    public static final String RESPONSE_IS_AWOL = "response.is.awol";

    public static final String NAME_AND_VALUE = "name.and.value";

    public static final String STORAGE_IS_NOT_WRITABLE = "storage.is.not.writable";

    public static final int INTEGER_NOT_REACHED = Integer.MIN_VALUE;

    public static final String STARTED_CONNECTION = "started.connection";

    public static final String STOPPED_CONNECTION = "stopped.connection";

    public static final String DELETED_CONNECTION = "deleted.connection";

    public static final String COMMAND_TIMEOUT_IS_AWOL = "command.timeout.is.awol";

    public static final String COMMAND_EXECUTION_TIMED_OUT = "command.execution.timed.out";

    public static final String HJB_RUNNER_IGNORED_A_COMMAND = "hjb.runner.ignored.a.command";

    public static final String HJB_RUNNER_FINISHED_AN_IGNORED_COMMAND = "hjb.runner.finished.an.ignored.command";

    public static final String COMMAND_TIMEOUT_TIMER_IS_AWOL = "command.timeout.timer.is.awol";

    public static final String TRIED_TO_EXECUTE_COMMAND_TWICE = "tried.to.execute.command.twice";

    public static final String PROVIDER_IS_INVALID = "provider.name.is.invalid";

    public static final String ENCODING_NOT_SUPPORTED = "encoding.not.supported";

    public static final String END_SHUTDOWN_PROVIDERS = "end.shutdown.providers";

    public static final String END_SHUTDOWN_1_PROVIDER = "end.shutdown.1.provider";

    public static final String START_SHUTDOWN_1_PROVIDER = "start.shutdown.1.provider";

    public static final String START_SHUTDOWN_PROVIDERS = "start.shutdown.providers";

    public static final String JUST_SCHEDULED_A_COMMAND = "just.scheduled.a.command";

    public static final String JUST_RAN_COMMAND = "just.ran.command";

    public static final String HANDLING_REQUEST = "handling.request";

    public static final String NOT_MATCHED = "not.matched";

    public static final String SHUTDOWN_NOT_SMOOTH = "shutdown.not.smooth";

    public static final String PROVIDER_ALREADY_REGISTERED = "provider.already.registered";

    public static final String NO_VALUE_PROVIDED = "no.value.provided";

    public static final String HJB_PATH_CONNECTION = "hjb.path.connection";
    
    public static final String HJB_PATH_DESTINATION = "hjb.path.destination";

    public static final String HJB_PATH_SESSION = "hjb.path.session";

    public static final String HJB_PATH_BROWSER = "hjb.path.browser";

    public static final String HJB_PATH_CONSUMER = "hjb.path.consumer";

    public static final String HJB_PATH_SUBSCRIBER = "hjb.path.subscriber";

    public static final String HJB_PATH_PRODUCER = "hjb.path.producer";

    public static final String FOUND_N_MESSAGES = "found.n.messages";

    public static final String RECEIVED_HJB_MESSAGE = "received.hjb.message";

    public static final String ABOUT_TO_SEND = "about.to.send";

    public static final String REQUEST_METHOD_AND_PATH = "request.method.and.path";

    public static final String COULD_NOT_READ_BOOLEAN_VALUE_FROM_MESSAGE = "could.not.read.boolean.value.from.message";

    public static final String SUCCESS_MESSAGE_OF_UNSUBSCRIBE = "success.message.of.unsubscribe";

    public static final String DESCRIPTION_OF_UNSUBSCRIBE = "description.of.unsubscribe";

    public static final String INVALID_CLIENT_ID = "invalid.client.id";

    public static final String COULD_NOT_SET_CLIENT_ID = "could.not.set.client.id";

    public static final String MESSAGE_WAS_SENT = "message.was.sent";

    public static final String FORMAT_CLIENT_ID = "format.client.id";
    
    public static final String SIMPLE_DELETE_SUCCESS_MESSAGE = "simple.delete.success.message";
    
    public static final String SIMPLE_DELETE_DESCRIPTION = "simple.delete.description";

    public static final String INVALID_SESSION_INDEX = "invalid.session.index";

    public static final String TRANSACTED = "transacted";
    
    public static final String SESSION_DESCRIPTION = "session.description";

    public static final String INVALID_PRODUCER_INDEX = "invalid.producer.index";

    public static final String INVALID_CONSUMER_INDEX = "invalid.consumer.index";

    public static final String INVALID_BROWSER_INDEX = "invalid.browser.index";

    public static final String INVALID_SUBSCRIBER_INDEX = "invalid.subscriber.index";

    public static final String INVALID_CONNECTION_INDEX = "invalid.connection.index";

    public static final String PRODUCER_DESCRIPTION = "producer.description";

    public static final String SUBSCRIBER_DESCRIPTION = "subscriber.description";

    public static final String CONSUMER_DESCRIPTION = "consumer.description";

    public static final String BROWSER_DESCRIPTION = "browser.description";

    public static final String ANY_PRODUCER = "any.producer";

    public static final String RUNNER_FOR = "runner.for";

    public static final String EXCEPTION_LOGFILE_NAME = "exception.logfile.name";

    public static final String COULD_NOT_READ_EXCEPTION_LOG = "could.not.read.exception.log";

    public static final String NO_ERRORS_WRITTEN = "no.errors.written";

    public static final String DESCRIPTION_OF_SHOW_ERROR_LOG = "description.of.show.error.log";

    public static final String SUCCESS_MESSAGE_OF_SHOW_ERROR_LOG = "success.message.of.show.error.log";

    public static final String CONNECTION_ERRORS_LOGGED = "connection.errors.logged";

    public static final String CAN_NOT_SET_SYSTEM_PROPERTIES = "can.not.set.system.properties";
}
