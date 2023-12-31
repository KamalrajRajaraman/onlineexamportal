/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
*/
package org.apache.ofbizcommon

import org.apache.ofbiz.service.ServiceUtil

logInfo('-=-=-=- TEST GROOVY SERVICE -=-=-=-')
result = ServiceUtil.returnSuccess()
if (context.message) {
    String message = context.message
    result.successMessage = (String) "Got message [$message] and finished fine"
    result.result = message
    logInfo("----- Message is: $message -----")
} else {
    result.successMessage = 'Got no message but finished fine anyway'
    result.result = '[no message received]'
    logInfo('----- No message received -----')
}
return result

// GroovyEngine will invoke the no-arg method.
Map testMethod() {
    logInfo('----- no-arg testMethod invoked -----')
    result = ServiceUtil.returnSuccess()
    if (context.message) {
        String message = context.message
        result.successMessage = (String) "Got message [$message] and finished fine"
        result.result = message
        logInfo("----- Message is: $message -----")
    } else {
        result.successMessage = 'Got no message but finished fine anyway'
        result.result = '[no message received]'
        logInfo('----- No message received -----')
    }
    return result
}

// ScriptEngine (JSR-223) will invoke the arg method.
Map testMethod(Map context) {
    logInfo('----- arg testMethod invoked -----')
    result = ServiceUtil.returnSuccess()
    if (context.message) {
        String message = context.message
        result.successMessage = (String) "Got message [$message] and finished fine"
        result.result = message
        logInfo("----- Message is: $message -----")
    } else {
        result.successMessage = 'Got no message but finished fine anyway'
        result.result = '[no message received]'
        logInfo('----- No message received -----')
    }
    return result
}
