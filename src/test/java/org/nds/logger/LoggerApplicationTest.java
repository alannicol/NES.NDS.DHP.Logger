package org.nds.logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nds.logger.appender.ListAppender;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ContextHierarchy(@ContextConfiguration)
class LoggerApplicationTest {

	private static LoggerContext loggerContext;
	private ListAppender logAppender;
	private ListAppender errorAppender;

	@BeforeAll
	public static void beforeAll() {
		loggerContext = (LoggerContext) LogManager.getContext(false);
	}

	@BeforeEach
	public void before() {
		logAppender = loggerContext.getConfiguration().getAppender("LogAppender");
		errorAppender = loggerContext.getConfiguration().getAppender("ErrorAppender");

		logAppender.clear();
		errorAppender.clear();
	}

	@Test
	void testAuditOne() {

		final String TEST_AUDIT = "testAuditOne";

		LoggerService.audit(TEST_AUDIT);

		assertLog(TEST_AUDIT, TEST_AUDIT);
	}

	@Test
	void testAuditTwo() {

		final String TEST_AUDIT = "testAuditTwo";

		LoggerService.audit(TEST_AUDIT, new AuditObject(TEST_AUDIT));

		assertLog(TEST_AUDIT, TEST_AUDIT + " " + TEST_AUDIT);
	}

	@Test
	void testAuditThreeFalse() {

		final String TEST_AUDIT = "testAuditThree";

		LoggerService.audit(TEST_AUDIT, false, new AuditObject(TEST_AUDIT));

		assertLog(TEST_AUDIT + " " + TEST_AUDIT, TEST_AUDIT + " " + TEST_AUDIT);
	}

	@Test
	void testAuditThreeTrue() {

		final String TEST_AUDIT = "testAuditThree";

		LoggerService.audit(TEST_AUDIT, true, new AuditObject(TEST_AUDIT));

		assertLog(TEST_AUDIT, TEST_AUDIT + " " + TEST_AUDIT);
	}

	@Test
	void testAuditFour() {

		final String TEST_AUDIT = "testAuditFour";

		try {
			throw new NullPointerException();
		} catch(NullPointerException nullPointerException) {
			LoggerService.error(TEST_AUDIT, nullPointerException);

			assertError(TEST_AUDIT, nullPointerException);
		}
	}

	private void assertLog(String infoMessage, String debugMessage) {
		assertLogEvent(logAppender.getEvent(0), Level.INFO, infoMessage);
		assertLogEvent(logAppender.getEvent(1), Level.DEBUG, debugMessage);
	}

	private void assertError(String message, Throwable throwable) {
		assertLogEvent(logAppender.getEvent(0), Level.ERROR, message, throwable);
		assertLogEvent(errorAppender.getEvent(0), Level.ERROR, message, throwable);
	}

	private void assertLogEvent(LogEvent logEvent, Level expectedLevel, String expectedMessage) {
		assertThat(logEvent.getLevel().name()).isEqualTo(expectedLevel.name());
		assertThat(logEvent.getMessage().getFormattedMessage()).isEqualTo(expectedMessage);
	}

	private void assertLogEvent(LogEvent logEvent, Level expectedLevel, String expectedMessage, Throwable throwable) {
		assertLogEvent(logEvent, expectedLevel, expectedMessage);
		assertThat(logEvent.getThrown().equals(throwable));
	}

	class AuditObject {

		private String message;

		public AuditObject(String message) {
			this.message = message;
		}

		@Override
		public String toString() {
			return message;
		}
	}

}
