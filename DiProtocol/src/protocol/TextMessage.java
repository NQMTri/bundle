package protocol;

/*
 * Copyright (C) 2017 Khoi Hoang
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 *
 * @author khoi
 */
public class TextMessage extends Message {
    
    private final String content;

    public TextMessage(String content) {
        this.content = content;
        timestamp = System.currentTimeMillis() / 1000L;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.TEXT_MESSAGE;
    }
}
