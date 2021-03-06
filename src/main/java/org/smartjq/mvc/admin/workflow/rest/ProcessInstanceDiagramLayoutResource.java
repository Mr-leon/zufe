/* Licensed under the Apache License, Version 2.0 (the "License");
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

package org.smartjq.mvc.admin.workflow.rest;

/***
 * 根据流程实例id获取整个流程图布局
 */
public class ProcessInstanceDiagramLayoutResource extends BaseProcessDefinitionDiagramLayoutResource {

  public void getDiagram() {
	  String processInstanceId = getPara("processInstanceId");
	  String callback = getPara("callback").toString();
//	  renderJson(callback+"("+getDiagramNode(processInstanceId, null).toString()+")");
  }
}
