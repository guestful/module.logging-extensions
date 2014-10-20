/**
 * Copyright (C) 2013 Guestful (info@guestful.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.guestful.logback;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.net.SMTPAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class FixedLogbackSMTPAppender extends SMTPAppender {
    @Override
    protected Layout<ILoggingEvent> makeSubjectLayout(String subjectStr) {
        if (subjectStr == null) {
            subjectStr = "%logger{20} - %m"; // logback default
        }
        PatternLayout pl = new PatternLayout() {
            @Override
            public String doLayout(ILoggingEvent event) {
                String subject = super.doLayout(event).replace('\r', ' ');
                int pos = subject.indexOf('\n');
                return pos == -1 ? subject : subject.substring(0, pos);
            }
        };
        pl.setContext(getContext());
        pl.setPattern(subjectStr);
        // we don't want a ThrowableInformationConverter appended
        // to the end of the converter chain
        // This fixes issue LBCLASSIC-67
        pl.setPostCompileProcessor(null);
        pl.start();
        return pl;
    }
}
