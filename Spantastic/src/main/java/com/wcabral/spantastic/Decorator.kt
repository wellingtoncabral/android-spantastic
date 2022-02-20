/*
 * Copyright (C) 2022 Wellington Cabral da Silva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wcabral.spantastic

import android.text.Spannable

/**
 * Class that represents a Decorator to be applied on the span.
 *
 * @property what The what parameter refers to the span to apply to the text
 * @property fillOnlyOnePosition Indicates if the span should fill only one position.
 * Used to replace an space " " with a `Span.image`.
 * @property start indicate the start portion of the text to which to apply the span.
 * if start == -1, means that the start position will be the first character of the text.
 * Otherwise, the start position will be applied with the current value.
 * @property end indicate the end portion of the text to which to apply the span.
 * if end == -1, means that the end position will be the last character of the text.
 * Otherwise, the end position will be applied with the current value.
 * @property flags determines whether the span should expand to include the inserted text.
 *
 * @see Span
 */
open class Decorator(
    val what: Any,
    val fillOnlyOnePosition: Boolean = false,
    var start: Int = -1,
    var end: Int = -1,
    var flags: Int = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
)
