package com.chujunjie.scamwisecampus.data.local.seed

import com.chujunjie.scamwisecampus.domain.model.ActionOption
import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.MessageType
import com.chujunjie.scamwisecampus.domain.model.RiskLevel
import com.chujunjie.scamwisecampus.domain.model.ScamCategory
import com.chujunjie.scamwisecampus.domain.model.Scenario
import com.chujunjie.scamwisecampus.domain.model.WarningSign

internal object PhishingScenarioSeed {

    val scenarios: List<Scenario> = listOf(
        Scenario(
            id = "phishing_easy_01",
            title = "Password Expiry Warning",
            category = ScamCategory.PHISHING,
            difficulty = Difficulty.EASY,
            messageType = MessageType.EMAIL,
            sender = "IT Support <security-update@example-mail.com>",
            subject = "Password expires today",
            messageBody = """
                Your university Microsoft 365 password expires today.
                Sign in immediately using the link below to prevent account deletion.
            """.trimIndent(),
            correctRiskLevel = RiskLevel.HIGH_RISK,
            warningSigns = listOf(
                WarningSign(
                    id = "phishing_easy_01_warning_01",
                    description = "The message threatens immediate account deletion.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "phishing_easy_01_warning_02",
                    description = "The sender uses an external email domain.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "phishing_easy_01_warning_03",
                    description = "The message directs the user to sign in through a supplied link.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "phishing_easy_01_warning_04",
                    description = "The message recommends opening the official university portal.",
                    isActualWarning = false
                )
            ),
            actionOptions = listOf(
                ActionOption(
                    id = "phishing_easy_01_action_01",
                    description = "Open the supplied link and enter the password.",
                    isSafeAction = false,
                    feedback = "The link may lead to a fake sign-in page."
                ),
                ActionOption(
                    id = "phishing_easy_01_action_02",
                    description = "Access Microsoft 365 through the official university portal.",
                    isSafeAction = true,
                    feedback = "The official portal provides a safer sign-in path."
                ),
                ActionOption(
                    id = "phishing_easy_01_action_03",
                    description = "Reply with the current password.",
                    isSafeAction = false,
                    feedback = "Passwords must never be sent by email."
                ),
                ActionOption(
                    id = "phishing_easy_01_action_04",
                    description = "Forward the link to classmates.",
                    isSafeAction = false,
                    feedback = "Potential phishing links should not be distributed."
                )
            ),
            explanation = """
                Account threats, external sender domains, and supplied sign-in
                links are common phishing indicators.
            """.trimIndent(),
            verificationAdvice = """
                Open the university portal using a saved bookmark or manually
                typed official address and check the account there.
            """.trimIndent()
        ),
        Scenario(
            id = "phishing_medium_01",
            title = "University Research Survey",
            category = ScamCategory.PHISHING,
            difficulty = Difficulty.MEDIUM,
            messageType = MessageType.EMAIL,
            sender = "research-study@students-university.org",
            subject = "Student wellbeing survey invitation",
            messageBody = """
                You are invited to complete a student wellbeing survey. Participants
                will receive a $15 voucher. Enter your student ID and open the survey
                using the link below.
            """.trimIndent(),
            correctRiskLevel = RiskLevel.NEEDS_VERIFICATION,
            warningSigns = listOf(
                WarningSign(
                    id = "phishing_medium_01_warning_01",
                    description = "The sender uses an external domain.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "phishing_medium_01_warning_02",
                    description = "The survey requests a student ID.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "phishing_medium_01_warning_03",
                    description = "The message offers an incentive.",
                    isActualWarning = false
                ),
                WarningSign(
                    id = "phishing_medium_01_warning_04",
                    description = "The message has a clear subject line.",
                    isActualWarning = false
                )
            ),
            actionOptions = listOf(
                ActionOption(
                    id = "phishing_medium_01_action_01",
                    description = "Open the link because the reward is small.",
                    isSafeAction = false,
                    feedback = "The size of the reward does not prove that the survey is legitimate."
                ),
                ActionOption(
                    id = "phishing_medium_01_action_02",
                    description = "Verify the researcher through the official university directory.",
                    isSafeAction = true,
                    feedback = "Independent verification can confirm whether the study is genuine."
                ),
                ActionOption(
                    id = "phishing_medium_01_action_03",
                    description = "Reply with the student ID before opening the survey.",
                    isSafeAction = false,
                    feedback = "Personal information should not be provided before verification."
                ),
                ActionOption(
                    id = "phishing_medium_01_action_04",
                    description = "Forward the link to classmates and ask them to test it.",
                    isSafeAction = false,
                    feedback = "Potentially unsafe links should not be distributed to other people."
                )
            ),
            explanation = """
                Research surveys and incentives can be legitimate, but the external
                domain and request for a student ID require independent verification.
            """.trimIndent(),
            verificationAdvice = """
                Search the university staff directory or research portal and confirm
                the study using official contact information.
            """.trimIndent()
        ),
        Scenario(
            id = "phishing_hard_01",
            title = "Library Renewal Reminder",
            category = ScamCategory.PHISHING,
            difficulty = Difficulty.HARD,
            messageType = MessageType.EMAIL,
            sender = "library@university.edu",
            subject = "Library loan due tomorrow",
            messageBody = """
                One of your library loans is due tomorrow. Open the official library
                application or visit your library account to renew the item.
                This message does not request a password or payment.
            """.trimIndent(),
            correctRiskLevel = RiskLevel.NO_CLEAR_THREAT,
            warningSigns = listOf(
                WarningSign(
                    id = "phishing_hard_01_warning_01",
                    description = "The message requests an immediate bank transfer.",
                    isActualWarning = false
                ),
                WarningSign(
                    id = "phishing_hard_01_warning_02",
                    description = "The sender asks for a password.",
                    isActualWarning = false
                ),
                WarningSign(
                    id = "phishing_hard_01_warning_03",
                    description = "The message contains a shortened external link.",
                    isActualWarning = false
                ),
                WarningSign(
                    id = "phishing_hard_01_warning_04",
                    description = "The sender domain matches the university domain.",
                    isActualWarning = false
                )
            ),
            actionOptions = listOf(
                ActionOption(
                    id = "phishing_hard_01_action_01",
                    description = "Reply with the university account password.",
                    isSafeAction = false,
                    feedback = "Passwords should never be sent by email."
                ),
                ActionOption(
                    id = "phishing_hard_01_action_02",
                    description = "Ignore all future library messages.",
                    isSafeAction = false,
                    feedback = "Automatically distrusting every message may cause genuine notices to be missed."
                ),
                ActionOption(
                    id = "phishing_hard_01_action_03",
                    description = "Open the official library application and check the account.",
                    isSafeAction = true,
                    feedback = "Using an official application is a safe way to confirm the reminder."
                ),
                ActionOption(
                    id = "phishing_hard_01_action_04",
                    description = "Forward the email to an unknown online forum.",
                    isSafeAction = false,
                    feedback = "Account-related messages should not be shared with unknown people."
                )
            ),
            explanation = """
                The message uses an official university domain, does not request
                sensitive information, and directs the user to an existing official service.
            """.trimIndent(),
            verificationAdvice = """
                Open the official library application or type the university library
                address manually instead of relying on forwarded links.
            """.trimIndent()
        )
    )
}
