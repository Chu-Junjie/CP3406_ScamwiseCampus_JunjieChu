package com.chujunjie.scamwisecampus.data.local.seed

import com.chujunjie.scamwisecampus.domain.model.ActionOption
import com.chujunjie.scamwisecampus.domain.model.Difficulty
import com.chujunjie.scamwisecampus.domain.model.MessageType
import com.chujunjie.scamwisecampus.domain.model.RiskLevel
import com.chujunjie.scamwisecampus.domain.model.ScamCategory
import com.chujunjie.scamwisecampus.domain.model.Scenario
import com.chujunjie.scamwisecampus.domain.model.WarningSign

internal object ParcelScenarioSeed {

    val scenarios: List<Scenario> = listOf(
        Scenario(
            id = "parcel_easy_01",
            title = "Missed Delivery Fee",
            category = ScamCategory.PARCEL,
            difficulty = Difficulty.EASY,
            messageType = MessageType.SMS,
            sender = "Delivery Service",
            messageBody = """
                We could not deliver your parcel. Pay the $1.90 redelivery fee
                within 12 hours: bit.ly/reschedule-now.
            """.trimIndent(),
            correctRiskLevel = RiskLevel.HIGH_RISK,
            warningSigns = listOf(
                WarningSign(
                    id = "parcel_easy_01_warning_01",
                    description = "The message uses a shortened link.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "parcel_easy_01_warning_02",
                    description = "The message requests a small urgent payment.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "parcel_easy_01_warning_03",
                    description = "The message does not include a verifiable tracking number.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "parcel_easy_01_warning_04",
                    description = "The message directs the user to the official courier application.",
                    isActualWarning = false
                )
            ),
            actionOptions = listOf(
                ActionOption(
                    id = "parcel_easy_01_action_01",
                    description = "Pay the fee through the shortened link.",
                    isSafeAction = false,
                    feedback = "Shortened links can hide a fraudulent payment page."
                ),
                ActionOption(
                    id = "parcel_easy_01_action_02",
                    description = "Open the courier's official application and check the tracking number.",
                    isSafeAction = true,
                    feedback = "The official application is a safer verification channel."
                ),
                ActionOption(
                    id = "parcel_easy_01_action_03",
                    description = "Reply with card details.",
                    isSafeAction = false,
                    feedback = "Card details should not be sent by SMS."
                ),
                ActionOption(
                    id = "parcel_easy_01_action_04",
                    description = "Forward the link to another person for testing.",
                    isSafeAction = false,
                    feedback = "Potentially malicious links should not be shared."
                )
            ),
            explanation = """
                Small delivery fees are used to make payment requests appear harmless.
                The shortened link and urgency increase the risk.
            """.trimIndent(),
            verificationAdvice = """
                Check the parcel using the courier's official application or website
                and a tracking number obtained independently.
            """.trimIndent()
        ),
        Scenario(
            id = "parcel_medium_01",
            title = "Customs Hold Email",
            category = ScamCategory.PARCEL,
            difficulty = Difficulty.MEDIUM,
            messageType = MessageType.EMAIL,
            sender = "clearance@international-shipping.example",
            subject = "Parcel awaiting customs information",
            messageBody = """
                Your international parcel is awaiting customs clearance.
                Submit your identification number and pay any applicable duty
                through the attached portal. A reference number is provided.
            """.trimIndent(),
            correctRiskLevel = RiskLevel.NEEDS_VERIFICATION,
            warningSigns = listOf(
                WarningSign(
                    id = "parcel_medium_01_warning_01",
                    description = "The message requests an identification number.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "parcel_medium_01_warning_02",
                    description = "The payment portal has not been independently verified.",
                    isActualWarning = true
                ),
                WarningSign(
                    id = "parcel_medium_01_warning_03",
                    description = "The message includes a reference number.",
                    isActualWarning = false
                ),
                WarningSign(
                    id = "parcel_medium_01_warning_04",
                    description = "International parcels may sometimes require customs processing.",
                    isActualWarning = false
                )
            ),
            actionOptions = listOf(
                ActionOption(
                    id = "parcel_medium_01_action_01",
                    description = "Submit identification through the attached portal.",
                    isSafeAction = false,
                    feedback = "Sensitive information should not be submitted before the request is verified."
                ),
                ActionOption(
                    id = "parcel_medium_01_action_02",
                    description = "Verify the reference number through the courier's official website.",
                    isSafeAction = true,
                    feedback = "Independent tracking can confirm whether customs action is genuinely required."
                ),
                ActionOption(
                    id = "parcel_medium_01_action_03",
                    description = "Pay first and ask questions later.",
                    isSafeAction = false,
                    feedback = "Payment should not be made before the request is confirmed."
                ),
                ActionOption(
                    id = "parcel_medium_01_action_04",
                    description = "Send a passport copy by reply email.",
                    isSafeAction = false,
                    feedback = "Email is not an appropriate channel for unnecessary identity documents."
                )
            ),
            explanation = """
                Customs requests can be legitimate, but identity and payment requests
                must be confirmed using the courier's official tracking service.
            """.trimIndent(),
            verificationAdvice = """
                Enter the tracking or reference number on the courier's official
                website and contact customs through verified details if needed.
            """.trimIndent()
        ),
        Scenario(
            id = "parcel_hard_01",
            title = "Official Delivery Update",
            category = ScamCategory.PARCEL,
            difficulty = Difficulty.HARD,
            messageType = MessageType.SMS,
            sender = "Trusted Courier",
            messageBody = """
                Parcel 8452 is out for delivery today. No payment is required.
                Manage delivery preferences inside the courier application you
                already use.
            """.trimIndent(),
            correctRiskLevel = RiskLevel.NO_CLEAR_THREAT,
            warningSigns = listOf(
                WarningSign(
                    id = "parcel_hard_01_warning_01",
                    description = "The message requests card details.",
                    isActualWarning = false
                ),
                WarningSign(
                    id = "parcel_hard_01_warning_02",
                    description = "The message includes a shortened external link.",
                    isActualWarning = false
                ),
                WarningSign(
                    id = "parcel_hard_01_warning_03",
                    description = "The message states that no payment is required.",
                    isActualWarning = false
                ),
                WarningSign(
                    id = "parcel_hard_01_warning_04",
                    description = "Delivery preferences can be checked in an existing official application.",
                    isActualWarning = false
                )
            ),
            actionOptions = listOf(
                ActionOption(
                    id = "parcel_hard_01_action_01",
                    description = "Open the existing courier application and confirm the tracking update.",
                    isSafeAction = true,
                    feedback = "The official application provides a safe confirmation path."
                ),
                ActionOption(
                    id = "parcel_hard_01_action_02",
                    description = "Reply with card details in case a fee is required.",
                    isSafeAction = false,
                    feedback = "The message does not request payment, and card details should not be sent by SMS."
                ),
                ActionOption(
                    id = "parcel_hard_01_action_03",
                    description = "Assume the update is a scam without checking.",
                    isSafeAction = false,
                    feedback = "Legitimate delivery updates exist and can be verified safely."
                ),
                ActionOption(
                    id = "parcel_hard_01_action_04",
                    description = "Share the tracking number publicly.",
                    isSafeAction = false,
                    feedback = "Tracking information should not be shared unnecessarily."
                )
            ),
            explanation = """
                The update requests no money or credentials and directs the user
                to an existing official application rather than an unknown link.
            """.trimIndent(),
            verificationAdvice = """
                Confirm the status inside the courier application already installed
                on the device.
            """.trimIndent()
        )
    )
}
