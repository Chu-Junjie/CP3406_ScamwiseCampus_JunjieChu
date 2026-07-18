package com.chujunjie.scamwisecampus.data.local.seed

import com.chujunjie.scamwisecampus.domain.model.ActionOption
import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.MessageType
import com.chujunjie.scamwisecampus.domain.model.RiskLevel
import com.chujunjie.scamwisecampus.domain.model.ScamCategory
import com.chujunjie.scamwisecampus.domain.model.Scenario
import com.chujunjie.scamwisecampus.domain.model.WarningSign

internal object ImpersonationScenarioSeed {

    val scenarios: List<Scenario> = listOf(
        Scenario(
            id = "impersonation_easy_01",
            title = "Friend's Emergency Transfer",
            category = ScamCategory.IMPERSONATION,
            difficulty = Difficulty.EASY,
            messageType = MessageType.CHAT,
            sender = "New Number",
            messageBody = """
                My phone is broken and I am using a new number. I urgently need
                $300 for a medical bill. Please transfer it now and do not call
                because I am in the hospital.
            """.trimIndent(),
            correctRiskLevel = RiskLevel.HIGH_RISK,
            warningSigns = listOf(
                WarningSign(
                    id = "impersonation_easy_01_warning_01",
                    description = "The sender claims to be using a new number.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "impersonation_easy_01_warning_02",
                    description = "The message requests an urgent transfer.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "impersonation_easy_01_warning_03",
                    description = "The sender discourages voice verification.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "impersonation_easy_01_warning_04",
                    description = "The sender suggests contacting the friend through a known account.",
                    isActualWarning = false
                )
            ),
            actionOptions = listOf(
                ActionOption(
                    id = "impersonation_easy_01_action_01",
                    description = "Transfer the money immediately.",
                    isSafeAction = false,
                    feedback = "Urgency should not replace identity verification."
                ),
                ActionOption(
                    id = "impersonation_easy_01_action_02",
                    description = "Contact the friend through a previously known number or account.",
                    isSafeAction = true,
                    feedback = "A separate trusted channel can confirm the person's identity."
                ),
                ActionOption(
                    id = "impersonation_easy_01_action_03",
                    description = "Send card details instead of money.",
                    isSafeAction = false,
                    feedback = "Card details are highly sensitive and should never be shared."
                ),
                ActionOption(
                    id = "impersonation_easy_01_action_04",
                    description = "Ask the sender to request a larger amount later.",
                    isSafeAction = false,
                    feedback = "Changing the amount does not verify the sender."
                )
            ),
            explanation = """
                New-number claims, emotional urgency, and requests to avoid calling
                are common signs of friend or family impersonation scams.
            """.trimIndent(),
            verificationAdvice = """
                Contact the person using an existing number, social account, or
                another trusted mutual contact.
            """.trimIndent()
        ),
        Scenario(
            id = "impersonation_medium_01",
            title = "Student Society President",
            category = ScamCategory.IMPERSONATION,
            difficulty = Difficulty.MEDIUM,
            messageType = MessageType.EMAIL,
            sender = "president.studentclub@example-mail.com",
            subject = "Urgent purchase for tonight's event",
            messageBody = """
                I am in a meeting and need you to buy five digital gift cards
                for tonight's event. Send the card codes by email and I will
                reimburse you tomorrow.
            """.trimIndent(),
            correctRiskLevel = RiskLevel.NEEDS_VERIFICATION,
            warningSigns = listOf(
                WarningSign(
                    id = "impersonation_medium_01_warning_01",
                    description = "The sender requests digital gift cards.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "impersonation_medium_01_warning_02",
                    description = "The sender uses an unfamiliar email domain.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "impersonation_medium_01_warning_03",
                    description = "The sender claims to be unavailable for direct confirmation.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "impersonation_medium_01_warning_04",
                    description = "Student societies sometimes organise events.",
                    isActualWarning = false
                )
            ),
            actionOptions = listOf(
                ActionOption(
                    id = "impersonation_medium_01_action_01",
                    description = "Buy the cards and send the codes.",
                    isSafeAction = false,
                    feedback = "Gift-card codes are difficult to recover once shared."
                ),
                ActionOption(
                    id = "impersonation_medium_01_action_02",
                    description = "Confirm the request using the society's known group chat or phone number.",
                    isSafeAction = true,
                    feedback = "A separate known channel can verify the request."
                ),
                ActionOption(
                    id = "impersonation_medium_01_action_03",
                    description = "Ask for reimbursement details after sending the codes.",
                    isSafeAction = false,
                    feedback = "Verification must happen before any purchase."
                ),
                ActionOption(
                    id = "impersonation_medium_01_action_04",
                    description = "Forward the request to another member and ask them to pay.",
                    isSafeAction = false,
                    feedback = "Passing an unverified request to another person spreads the risk."
                )
            ),
            explanation = """
                Gift-card requests and claims of temporary unavailability are common
                impersonation tactics, but the identity should still be verified.
            """.trimIndent(),
            verificationAdvice = """
                Contact the society president through an existing group chat,
                known phone number, or another committee member.
            """.trimIndent()
        ),
        Scenario(
            id = "impersonation_hard_01",
            title = "Classmate's New Number",
            category = ScamCategory.IMPERSONATION,
            difficulty = Difficulty.HARD,
            messageType = MessageType.CHAT,
            sender = "Unknown Number",
            messageBody = """
                Hi, this is Maya from your database class. I changed my number.
                Could you send me the lecture notes from yesterday? I can tell
                you the tutorial group and assignment topic if needed.
            """.trimIndent(),
            correctRiskLevel = RiskLevel.NEEDS_VERIFICATION,
            warningSigns = listOf(
                WarningSign(
                    id = "impersonation_hard_01_warning_01",
                    description = "The sender uses an unknown number.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "impersonation_hard_01_warning_02",
                    description = "The sender claims to have changed numbers.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "impersonation_hard_01_warning_03",
                    description = "The request is limited to lecture notes.",
                    isActualWarning = false
                ),
                WarningSign(
                    id = "impersonation_hard_01_warning_04",
                    description = "The sender offers course-specific details for verification.",
                    isActualWarning = false
                )
            ),
            actionOptions = listOf(
                ActionOption(
                    id = "impersonation_hard_01_action_01",
                    description = "Send the entire class contact list.",
                    isSafeAction = false,
                    feedback = "Other students' contact information should not be shared."
                ),
                ActionOption(
                    id = "impersonation_hard_01_action_02",
                    description = "Verify the identity through the existing class group before sharing notes.",
                    isSafeAction = true,
                    feedback = "A low-risk request can still be verified through a known channel."
                ),
                ActionOption(
                    id = "impersonation_hard_01_action_03",
                    description = "Send account passwords to prove friendship.",
                    isSafeAction = false,
                    feedback = "Passwords must never be shared."
                ),
                ActionOption(
                    id = "impersonation_hard_01_action_04",
                    description = "Report the number as criminal without checking.",
                    isSafeAction = false,
                    feedback = "The message is plausible and should be verified before making accusations."
                )
            ),
            explanation = """
                The request may be genuine and is not highly sensitive, but the
                unknown number and identity claim still justify verification.
            """.trimIndent(),
            verificationAdvice = """
                Confirm the sender through an existing class group, known account,
                or a shared course detail that is not sensitive.
            """.trimIndent()
        )
    )
}
