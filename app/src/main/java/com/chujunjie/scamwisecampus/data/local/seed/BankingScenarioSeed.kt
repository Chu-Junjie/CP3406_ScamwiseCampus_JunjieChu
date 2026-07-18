package com.chujunjie.scamwisecampus.data.local.seed

import com.chujunjie.scamwisecampus.domain.model.ActionOption
import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.MessageType
import com.chujunjie.scamwisecampus.domain.model.RiskLevel
import com.chujunjie.scamwisecampus.domain.model.ScamCategory
import com.chujunjie.scamwisecampus.domain.model.Scenario
import com.chujunjie.scamwisecampus.domain.model.WarningSign

internal object BankingScenarioSeed {

    val scenarios: List<Scenario> = listOf(
        Scenario(
            id = "banking_easy_01",
            title = "Account Suspension Alert",
            category = ScamCategory.BANKING,
            difficulty = Difficulty.EASY,
            messageType = MessageType.SMS,
            sender = "Bank Security",
            messageBody = """
                Your bank account will be suspended within 30 minutes.
                Confirm your identity immediately at secure-bank-login.co.
            """.trimIndent(),
            correctRiskLevel = RiskLevel.HIGH_RISK,
            warningSigns = listOf(
                WarningSign(
                    id = "banking_easy_01_warning_01",
                    description = "The message creates an urgent deadline.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "banking_easy_01_warning_02",
                    description = "The link does not clearly match an official bank domain.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "banking_easy_01_warning_03",
                    description = "The message asks the user to confirm identity through a link.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "banking_easy_01_warning_04",
                    description = "The message recommends opening the official banking application.",
                    isActualWarning = false
                )
            ),
            actionOptions = listOf(
                ActionOption(
                    id = "banking_easy_01_action_01",
                    description = "Open the supplied link and sign in.",
                    isSafeAction = false,
                    feedback = "The supplied link may lead to a fake banking page."
                ),
                ActionOption(
                    id = "banking_easy_01_action_02",
                    description = "Reply with the account number.",
                    isSafeAction = false,
                    feedback = "Account information should not be sent by SMS."
                ),
                ActionOption(
                    id = "banking_easy_01_action_03",
                    description = "Open the official banking application and check for alerts.",
                    isSafeAction = true,
                    feedback = "The official application provides a safer verification path."
                ),
                ActionOption(
                    id = "banking_easy_01_action_04",
                    description = "Forward the message to friends and ask them to sign in first.",
                    isSafeAction = false,
                    feedback = "Potentially malicious links should not be shared."
                )
            ),
            explanation = """
                The urgent deadline, suspicious domain, and identity request are
                strong indicators of a banking phishing attempt.
            """.trimIndent(),
            verificationAdvice = """
                Open the official banking application or call the verified number
                printed on the bank card.
            """.trimIndent()
        ),
        Scenario(
            id = "banking_medium_01",
            title = "Fraud Team Phone Call",
            category = ScamCategory.BANKING,
            difficulty = Difficulty.MEDIUM,
            messageType = MessageType.PHONE_CALL,
            sender = "Bank Fraud Team",
            messageBody = """
                A caller says your account is being used by criminals. They ask
                you to move all funds to a temporary safe account while the bank
                investigates. They say you must stay on the call.
            """.trimIndent(),
            correctRiskLevel = RiskLevel.HIGH_RISK,
            warningSigns = listOf(
                WarningSign(
                    id = "banking_medium_01_warning_01",
                    description = "The caller asks the user to move money to another account.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "banking_medium_01_warning_02",
                    description = "The caller prevents independent verification by keeping the user on the line.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "banking_medium_01_warning_03",
                    description = "The caller uses fear to create urgency.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "banking_medium_01_warning_04",
                    description = "The caller advises contacting the bank through a verified number.",
                    isActualWarning = false
                )
            ),
            actionOptions = listOf(
                ActionOption(
                    id = "banking_medium_01_action_01",
                    description = "Transfer the money while remaining on the call.",
                    isSafeAction = false,
                    feedback = "Banks do not protect funds by asking customers to transfer money to a safe account."
                ),
                ActionOption(
                    id = "banking_medium_01_action_02",
                    description = "End the call and contact the bank using a verified number.",
                    isSafeAction = true,
                    feedback = "Ending the call allows independent verification."
                ),
                ActionOption(
                    id = "banking_medium_01_action_03",
                    description = "Share the one-time password to prove identity.",
                    isSafeAction = false,
                    feedback = "One-time passwords must never be shared."
                ),
                ActionOption(
                    id = "banking_medium_01_action_04",
                    description = "Install the remote-support application suggested by the caller.",
                    isSafeAction = false,
                    feedback = "Remote-support software can give an attacker control of the device."
                )
            ),
            explanation = """
                Requests to move money to a safe account are a common impersonation
                tactic. Keeping the victim on the line prevents independent checks.
            """.trimIndent(),
            verificationAdvice = """
                End the call, wait briefly, and contact the bank through its official
                application or a verified phone number.
            """.trimIndent()
        ),
        Scenario(
            id = "banking_hard_01",
            title = "Scheduled Maintenance Notice",
            category = ScamCategory.BANKING,
            difficulty = Difficulty.HARD,
            messageType = MessageType.EMAIL,
            sender = "notifications@yourbank.example",
            subject = "Planned mobile banking maintenance",
            messageBody = """
                Mobile banking will be unavailable from 1:00 a.m. to 3:00 a.m.
                on Sunday for planned maintenance. No action is required.
                Service updates are available inside the official banking application.
            """.trimIndent(),
            correctRiskLevel = RiskLevel.NO_CLEAR_THREAT,
            warningSigns = listOf(
                WarningSign(
                    id = "banking_hard_01_warning_01",
                    description = "The message requests a password.",
                    isActualWarning = false
                ),
                WarningSign(
                    id = "banking_hard_01_warning_02",
                    description = "The message requests an immediate transfer.",
                    isActualWarning = false
                ),
                WarningSign(
                    id = "banking_hard_01_warning_03",
                    description = "The message says no action is required.",
                    isActualWarning = false
                ),
                WarningSign(
                    id = "banking_hard_01_warning_04",
                    description = "The update can be checked inside the official application.",
                    isActualWarning = false
                )
            ),
            actionOptions = listOf(
                ActionOption(
                    id = "banking_hard_01_action_01",
                    description = "Check the service notice inside the official banking application.",
                    isSafeAction = true,
                    feedback = "The official application provides an independent confirmation path."
                ),
                ActionOption(
                    id = "banking_hard_01_action_02",
                    description = "Reply with account credentials.",
                    isSafeAction = false,
                    feedback = "Credentials should never be sent by email."
                ),
                ActionOption(
                    id = "banking_hard_01_action_03",
                    description = "Transfer funds before the maintenance begins.",
                    isSafeAction = false,
                    feedback = "The notice does not require moving money."
                ),
                ActionOption(
                    id = "banking_hard_01_action_04",
                    description = "Assume every maintenance notice is fraudulent.",
                    isSafeAction = false,
                    feedback = "Legitimate service notices exist and should be checked through official channels."
                )
            ),
            explanation = """
                The message is informational, requests no action or sensitive data,
                and directs the user to an existing official service for confirmation.
            """.trimIndent(),
            verificationAdvice = """
                Confirm the maintenance notice inside the official banking application
                if reassurance is needed.
            """.trimIndent()
        )
    )
}
